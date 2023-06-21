package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.dto.GroupUpdate;
import xcode.parhepengon.domain.mapper.GroupMapper;
import xcode.parhepengon.domain.mapper.GroupMemberMapper;
import xcode.parhepengon.domain.model.*;
import xcode.parhepengon.domain.repository.*;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.domain.response.group.GroupDetailResponse;
import xcode.parhepengon.domain.response.group.GroupResponse;
import xcode.parhepengon.domain.response.group.MemberResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.GroupPresenter;

import java.math.BigDecimal;
import java.util.*;

import static xcode.parhepengon.domain.enums.GroupHistoryEventEnum.*;
import static xcode.parhepengon.shared.ResponseCode.*;

@Service
public class GroupService implements GroupPresenter {
    @Autowired
    private HistoryService historyService;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BillMemberRepository billMemberRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private GroupHistoryRepository groupHistoryRepository;

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

            historyService.addGroupHistory(groupMapper.createGroupHistory(CREATE_GROUP, null, model.getSecureId(), profileService.getUserFullName()));

            response.setSuccess(new SecureIdResponse(model.getSecureId()));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> edit(CreateGroupRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        GroupModel model = getGroupOwner(request.getSecureId());
        GroupUpdate groupUpdate = groupMapper.createGroupHistory(EDIT_GROUP, model, model.getSecureId(), profileService.getUserFullName());

        try {
            GroupModel updated = groupMapper.editModel(model, request);
            groupUpdate = groupMapper.setNewGroup(updated, groupUpdate);

            groupRepository.save(updated);
            historyService.addGroupHistory(groupUpdate);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> delete(BaseRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        GroupModel model = getGroupOwner(request.getSecureId());

        try {
            groupRepository.save(groupMapper.deleteModel(model));

            List<GroupMemberModel> groupMemberModels = groupMemberService.getMemberList(model.getSecureId());
            groupMemberModels.forEach(e -> e.setLeaveAt(new Date()));

            groupMemberRepository.saveAll(groupMemberModels);

            historyService.addGroupHistory(groupMapper.createGroupHistory(DELETE_GROUP, null, "", profileService.getUserFullName()));

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<List<MemberResponse>> getMemberList(BaseRequest request) {
        BaseResponse<List<MemberResponse>> response = new BaseResponse<>();

        Optional<GroupModel> model = groupRepository.getGroup(request.getSecureId());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            List<GroupMemberModel> memberModels = groupMemberService.getMemberList(request.getSecureId());
            List<MemberResponse> memberResponses = new ArrayList<>();
            memberModels.forEach(e -> {
                Optional<ProfileModel> profileModel = profileRepository.getProfileBySecureId(e.getMember());
                memberResponses.add(groupMapper.memberModelToResponse(e, profileModel.orElse(null)));
            });

            response.setSuccess(memberResponses);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<List<GroupResponse>> getList() {
        BaseResponse<List<GroupResponse>> response = new BaseResponse<>();

        List<GroupModel> groupModels = groupRepository.getOwnerGroups(CurrentUser.get().getUserSecureId());

        if (groupModels.isEmpty()) {
            response.setSuccess(Collections.emptyList());
        } else {
            try {
                List<GroupResponse> memberModels = groupMapper.generateGroupResponse(groupModels);
                memberModels.forEach(e -> e.setTotalBalance(calculateUserBalance(billRepository.getGroupBills(e.getSecureId()))));

                response.setSuccess(memberModels);
            } catch (Exception e) {
                throw new AppException(e.toString());
            }
        }

        return response;
    }

    @Override
    public BaseResponse<GroupDetailResponse> detail(BaseRequest request) {
        BaseResponse<GroupDetailResponse> response = new BaseResponse<>();

        Optional<GroupModel> groupModel = groupRepository.getGroup(request.getSecureId());

        if (groupModel.isEmpty()) {
            throw new AppException(GROUP_NOT_FOUND_MESSAGE);
        }

        try {
            List<GroupHistoryModel> groupHistoryModels = groupHistoryRepository.getGroupHistory(request.getSecureId());
            List<GroupMemberModel> groupMemberModels = groupMemberRepository.getGroupMemberList(request.getSecureId());
            List<BillModel> groupBillModels = billRepository.getGroupBills(request.getSecureId());

            GroupDetailResponse groups = groupMapper.generateGroupDetailResponse(groupModel.get(), groupMemberModels, groupHistoryModels, groupBillModels);
            groups.setCreatedBy(profileService.getUserFullName(groupModel.get().getOwner()));

            for (int i=0; i<groupHistoryModels.size(); i++) {
                groups.getHistory().get(i).setCreatedBy(profileService.getUserFullName(groupHistoryModels.get(i).getCreatedBy()));
            }

            for (int i=0; i<groupMemberModels.size(); i++) {
                groups.getMember().get(i).setName(profileService.getUserFullName(groupMemberModels.get(i).getMember()));
            }

            for (int i=0; i<groupMemberModels.size(); i++) {
                groups.getBills().get(i).setPrepaidBy(profileService.getUserFullName(groupBillModels.get(i).getPrepaidBy()));
            }

            response.setSuccess(groups);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;    }

    public GroupModel getGroupOwner(String secureId) {
        Optional<GroupModel> model = groupRepository.getGroup(secureId);

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        if (!Objects.equals(model.get().getOwner(), CurrentUser.get().getUserSecureId())) {
            throw new AppException(NOT_AUTHORIZED_MESSAGE);
        }

        return model.get();
    }

    private BigDecimal calculateUserBalance(List<BillModel> bill) {
        BigDecimal result = BigDecimal.ZERO;

        for (BillModel b : bill) {
            List<BillMemberModel> billMemberModels = billMemberRepository.getBillMemberList(b.getSecureId());

            for (BillMemberModel m : billMemberModels) {
                if (Objects.equals(m.getMember(), CurrentUser.get().getUserSecureId())) {
                    result = m.isSettle() ? result.add(m.getAmount()) : result.subtract(m.getAmount());
                }
            }
        }

        return result;
    }
}
