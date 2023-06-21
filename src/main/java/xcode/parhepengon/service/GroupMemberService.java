package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.dto.GroupUpdate;
import xcode.parhepengon.domain.mapper.GroupMapper;
import xcode.parhepengon.domain.mapper.GroupMemberMapper;
import xcode.parhepengon.domain.model.*;
import xcode.parhepengon.domain.repository.GroupMemberRepository;
import xcode.parhepengon.domain.repository.GroupRepository;
import xcode.parhepengon.domain.repository.UserRepository;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.group.AddKickMemberRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.GroupMemberPresenter;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static xcode.parhepengon.domain.enums.GroupHistoryEventEnum.*;
import static xcode.parhepengon.shared.ResponseCode.NOT_FOUND_MESSAGE;
import static xcode.parhepengon.shared.ResponseCode.USER_EXIST_ON_GROUP;

@Service
public class GroupMemberService implements GroupMemberPresenter {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    @Lazy
    private GroupService groupService;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    private final GroupMemberMapper groupMemberMapper = new GroupMemberMapper();
    private final GroupMapper groupMapper = new GroupMapper();

    @Override
    public BaseResponse<Boolean> add(AddKickMemberRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<GroupMemberModel> model = groupMemberRepository.getGroupMember(request.getGroup(), request.getMember());
        Optional<UserModel> user = userRepository.getActiveUserBySecureId(request.getMember());

        if (model.isPresent()) {
            throw new AppException(USER_EXIST_ON_GROUP);
        }

        if (user.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            groupMemberRepository.save(groupMemberMapper.addRequestToModel(request));

            GroupUpdate groupUpdate = groupMapper.createGroupHistory(
                    ADD_MEMBER,
                    null,
                    model.get().getGroup(),
                    profileService.getUserFullName()
            );
            groupUpdate.setAddedUserName(profileService.getUserFullName(request.getMember()));

            historyService.addGroupHistory(groupUpdate);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> kick(AddKickMemberRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<GroupMemberModel> model = groupMemberRepository.getGroupMember(request.getGroup(), request.getMember());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            groupMemberRepository.save(groupMemberMapper.kickRequestToModel(model.get()));

            GroupUpdate groupUpdate = groupMapper.createGroupHistory(
                    KICK_MEMBER,
                    null,
                    model.get().getGroup(),
                    profileService.getUserFullName(),
                    profileService.getUserFullName(model.get().getMember())
            );
            groupUpdate.setDeletedUserName(profileService.getUserFullName(request.getMember()));

            historyService.addGroupHistory(groupUpdate);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> leave(BaseRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<GroupMemberModel> model = groupMemberRepository.getGroupMember(request.getSecureId(), CurrentUser.get().getUserSecureId());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            model.get().setLeaveAt(new Date());
            groupMemberRepository.save(model.get());

            GroupModel groupOwner = groupService.getGroupOwner(request.getSecureId());

            if (groupOwner != null) {
                if (groupMemberRepository.countGroupMember(request.getSecureId()) == 0) {
                    groupOwner.setDeletedAt(new Date());
                } else {
                    groupOwner.setOwner(groupMemberRepository.getNewOwner(request.getSecureId(), CurrentUser.get().getSecureId()).getMember());
                }

                groupRepository.save(groupOwner);
            }

            GroupUpdate groupUpdate = groupMapper.createGroupHistory(
                    LEAVE_GROUP,
                    null,
                    model.get().getGroup(),
                    profileService.getUserFullName()
            );

            historyService.addGroupHistory(groupUpdate);

            groupUpdate.setEvent(CHANGE_OWNER);
            groupUpdate.setNewOwner(groupOwner != null ? groupOwner.getOwner() : null);

            historyService.addGroupHistory(groupUpdate);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public List<GroupMemberModel> getMemberList(String secureId) {
        return groupMemberRepository.getGroupMemberList(secureId).orElse(Collections.emptyList());
    }
}
