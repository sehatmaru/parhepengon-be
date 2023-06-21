package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.dto.GroupUpdate;
import xcode.parhepengon.domain.enums.GroupHistoryEventEnum;
import xcode.parhepengon.domain.model.*;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;
import xcode.parhepengon.domain.response.group.MemberResponse;

import java.util.Date;

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

}
