package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.mapper.GroupMemberMapper;
import xcode.parhepengon.domain.model.GroupMemberModel;
import xcode.parhepengon.domain.model.UserModel;
import xcode.parhepengon.domain.repository.GroupMemberRepository;
import xcode.parhepengon.domain.repository.UserRepository;
import xcode.parhepengon.domain.request.group.AddKickMemberRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.GroupMemberPresenter;

import java.util.Optional;

import static xcode.parhepengon.domain.enums.EventEnum.ADD_MEMBER;
import static xcode.parhepengon.domain.enums.EventEnum.KICK_MEMBER;
import static xcode.parhepengon.shared.ResponseCode.NOT_FOUND_MESSAGE;
import static xcode.parhepengon.shared.ResponseCode.USER_EXIST_ON_GROUP;

@Service
public class GroupMemberService implements GroupMemberPresenter {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    private final GroupMemberMapper groupMemberMapper = new GroupMemberMapper();

    @Override
    public BaseResponse<Boolean> add(AddKickMemberRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<GroupMemberModel> model = groupMemberRepository.findByGroupAndMemberAndLeaveAtIsNull(request.getGroup(), request.getMember());
        Optional<UserModel> user = userRepository.findBySecureIdAndActiveIsTrueAndDeletedAtIsNull(request.getMember());

        if (model.isPresent()) {
            throw new AppException(USER_EXIST_ON_GROUP);
        }

        if (user.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            groupMemberRepository.save(groupMemberMapper.addRequestToModel(request));

            historyService.addHistory(ADD_MEMBER, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> kick(AddKickMemberRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<GroupMemberModel> model = groupMemberRepository.findByGroupAndMemberAndLeaveAtIsNull(request.getGroup(), request.getMember());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            groupMemberRepository.save(groupMemberMapper.kickRequestToModel(model.get()));

            historyService.addHistory(KICK_MEMBER, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }
}
