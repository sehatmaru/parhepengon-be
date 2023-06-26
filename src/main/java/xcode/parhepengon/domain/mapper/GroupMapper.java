package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.dto.GroupUpdate;
import xcode.parhepengon.domain.enums.GroupHistoryEventEnum;
import xcode.parhepengon.domain.model.*;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;
import xcode.parhepengon.domain.response.auth.UserResponse;
import xcode.parhepengon.domain.response.group.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.parhepengon.shared.Utils.generateSecureId;

public class GroupMapper {
    public GroupModel createRequestToModel(CreateGroupRequest request) {
        if (request != null) {
            GroupModel model = new GroupModel();
            model.setSecureId(generateSecureId());
            model.setName(request.getGroupName());
            model.setCategory(request.getCategory());
            model.setOwner(CurrentUser.get().getUserSecureId());
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public GroupModel editModel(GroupModel model, CreateGroupRequest request) {
        if (model != null && request != null) {
            model.setName(request.getGroupName());
            model.setCategory(request.getCategory());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public GroupModel deleteModel(GroupModel model) {
        if (model != null) {
            model.setDeletedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public MemberResponse memberModelToResponse(GroupMemberModel memberModel, ProfileModel profileModel) {
        if (memberModel != null && profileModel != null) {
            MemberResponse response = new MemberResponse();
            response.setFullName(profileModel.getFullName());
            response.setJoinedAt(memberModel.getJoinedAt());
            response.setSecureId(memberModel.getMember());

            return response;
        } else {
            return null;
        }
    }

    public UserResponse memberModelToUserResponse(UserModel memberModel, ProfileModel profileModel) {
        if (memberModel != null && profileModel != null) {
            UserResponse response = new UserResponse();
            response.setFullName(profileModel.getFullName());
            response.setSecureId(memberModel.getSecureId());

            return response;
        } else {
            return null;
        }
    }

    public GroupUpdate createGroupHistory(GroupHistoryEventEnum event, GroupModel oldGroup, String groupSecureId, String fullName) {
        GroupUpdate model = new GroupUpdate();
        model.setFullName(fullName);
        model.setGroupSecureId(groupSecureId);
        model.setEvent(event);

        if (oldGroup != null) {
            return setOldGroup(oldGroup, model);
        } else {
            return model;
        }
    }

    public GroupUpdate createGroupHistory(GroupHistoryEventEnum event, GroupModel oldGroup, String groupSecureId, String fullName, String kickedName) {
        GroupUpdate model = new GroupUpdate();
        model.setFullName(fullName);
        model.setGroupSecureId(groupSecureId);
        model.setEvent(event);
        model.setDeletedUserName(kickedName);

        if (oldGroup != null) {
            return setOldGroup(oldGroup, model);
        } else {
            return model;
        }
    }

    public GroupUpdate setOldGroup(GroupModel group, GroupUpdate groupUpdate) {
        if (group != null && groupUpdate != null) {
            groupUpdate.setOldOwner(group.getOwner());
            groupUpdate.setOldName(group.getName());
            groupUpdate.setOldCategory(group.getCategory());

            return groupUpdate;
        } else {
            return null;
        }
    }

    public GroupUpdate setNewGroup(GroupModel group, GroupUpdate groupUpdate) {
        if (group != null && groupUpdate != null) {
            groupUpdate.setNewOwner(group.getOwner());
            groupUpdate.setNewName(group.getName());
            groupUpdate.setNewCategory(group.getCategory());

            return groupUpdate;
        } else {
            return null;
        }
    }

    public List<GroupResponse> generateGroupResponse(List<GroupModel> groupModels) {
        List<GroupResponse> result = new ArrayList<>();

        if (groupModels != null && !groupModels.isEmpty()) {
            for (GroupModel group : groupModels) {
                GroupResponse response = new GroupResponse();
                response.setSecureId(group.getSecureId());
                response.setName(group.getName());
                response.setCategory(group.getCategory());

                result.add(response);
            }
        }

        return result;
    }

    public GroupDetailResponse generateGroupDetailResponse(GroupModel groupModel,
                                                           List<GroupMemberModel> groupMemberModels,
                                                           List<GroupHistoryModel> groupHistoryModels,
                                                           List<BillModel> groupBillModels) {
        GroupDetailResponse model = new GroupDetailResponse();

        if (groupModel != null) {
            model.setSecureId(groupModel.getSecureId());
            model.setName(groupModel.getName());
            model.setCategory(groupModel.getCategory());
            model.setCreatedAt(groupModel.getCreatedAt());
            model.setLastUpdated(groupModel.getUpdatedAt());
            model.setHistory(generateGroupHistoryResponse(groupHistoryModels));
            model.setMember(generateGroupMemberResponse(groupMemberModels));
            model.setBills(generateGroupBillResponse(groupBillModels));
        }

        return model;
    }

    public List<GroupHistoryResponse> generateGroupHistoryResponse(List<GroupHistoryModel> groups) {
        List<GroupHistoryResponse> result = new ArrayList<>();

        if (groups != null && !groups.isEmpty()) {
            for (GroupHistoryModel group : groups) {
                GroupHistoryResponse response = new GroupHistoryResponse();
                response.setEvent(group.getEvent());
                response.setComment(group.getComment());
                response.setCreatedAt(group.getCreatedAt());

                result.add(response);
            }
        }

        return result;
    }

    public List<GroupMemberResponse> generateGroupMemberResponse(List<GroupMemberModel> groups) {
        List<GroupMemberResponse> result = new ArrayList<>();

        if (groups != null && !groups.isEmpty()) {
            for (GroupMemberModel group : groups) {
                GroupMemberResponse response = new GroupMemberResponse();
                response.setJoinedAt(group.getJoinedAt());
                response.setSecureId(group.getSecureId());
                response.setLeaveAt(group.getLeaveAt());

                result.add(response);
            }
        }

        return result;
    }

    public List<GroupBillResponse> generateGroupBillResponse(List<BillModel> bills) {
        List<GroupBillResponse> result = new ArrayList<>();

        if (bills != null && !bills.isEmpty()) {
            for (BillModel bill : bills) {
                GroupBillResponse response = new GroupBillResponse();
                response.setTitle(bill.getTitle());
                response.setSecureId(bill.getSecureId());
                response.setAmount(bill.getTotalAmount());
                response.setSettle(bill.isSettle());

                result.add(response);
            }
        }

        return result;
    }

}
