package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.model.GroupMemberModel;
import xcode.parhepengon.domain.model.GroupModel;
import xcode.parhepengon.domain.model.ProfileModel;
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

}
