package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.mapper.GroupMapper;
import xcode.parhepengon.domain.mapper.GroupMemberMapper;
import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.model.GroupMemberModel;
import xcode.parhepengon.domain.model.GroupModel;
import xcode.parhepengon.domain.model.ProfileModel;
import xcode.parhepengon.domain.repository.GroupMemberRepository;
import xcode.parhepengon.domain.repository.GroupRepository;
import xcode.parhepengon.domain.repository.ProfileRepository;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.domain.response.group.MemberResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.GroupPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static xcode.parhepengon.domain.enums.EventEnum.*;
import static xcode.parhepengon.shared.ResponseCode.*;

@Service
public class GroupService implements GroupPresenter {
    @Autowired
    private HistoryService historyService;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private final GroupMapper groupMapper = new GroupMapper();
    private final GroupMemberMapper groupMemberMapper = new GroupMemberMapper();

    @Override
    public BaseResponse<SecureIdResponse> create(CreateGroupRequest request) {
        BaseResponse<SecureIdResponse> response = new BaseResponse<>();

        try {
            GroupModel model = groupMapper.createRequestToModel(request);
            GroupMemberModel memberModel = groupMemberMapper.createRequestToModel(request, model.getSecureId());

            groupRepository.save(model);
            groupMemberRepository.save(memberModel);

            historyService.addHistory(CREATE_GROUP, null);

            response.setSuccess(new SecureIdResponse(model.getSecureId()));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> edit(CreateGroupRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        GroupModel model = checkGroup(request.getSecureId());

        try {
            groupRepository.save(groupMapper.editModel(model, request));

            historyService.addHistory(EDIT_GROUP, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> delete(BaseRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        GroupModel model = checkGroup(request.getSecureId());

        try {
            groupRepository.save(groupMapper.deleteModel(model));

            historyService.addHistory(DELETE_GROUP, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<List<MemberResponse>> getMemberList(BaseRequest request) {
        BaseResponse<List<MemberResponse>> response = new BaseResponse<>();

        Optional<GroupModel> model = groupRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            List<GroupMemberModel> memberModels = groupMemberService.getMemberList(request.getSecureId());
            List<MemberResponse> memberResponses = new ArrayList<>();
            memberModels.forEach(e -> {
                Optional<ProfileModel> profileModel = profileRepository.findByUserAndDeletedAtIsNull(e.getMember());
                memberResponses.add(groupMapper.memberModelToResponse(e, profileModel.orElse(null)));
            });

            response.setSuccess(memberResponses);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    private GroupModel checkGroup(String secureId) {
        Optional<GroupModel> model = groupRepository.findBySecureIdAndDeletedAtIsNull(secureId);

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        if (!Objects.equals(model.get().getOwner(), CurrentUser.get().getUserSecureId())) {
            throw new AppException(NOT_AUTHORIZED_MESSAGE);
        }

        return model.get();
    }
}
