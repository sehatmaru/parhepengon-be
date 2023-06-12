package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.model.GroupMemberModel;
import xcode.parhepengon.domain.request.group.AddKickMemberRequest;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;

import java.util.Date;

import static xcode.parhepengon.shared.Utils.generateSecureId;

public class GroupMemberMapper {
    public GroupMemberModel createRequestToModel(CreateGroupRequest request, String groupSecureId) {
        if (request != null) {
            GroupMemberModel model = new GroupMemberModel();
            model.setSecureId(generateSecureId());
            model.setMember(CurrentUser.get().getUserSecureId());
            model.setGroup(groupSecureId);
            model.setJoinedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public GroupMemberModel addRequestToModel(AddKickMemberRequest request) {
        if (request != null) {
            GroupMemberModel model = new GroupMemberModel();
            model.setSecureId(generateSecureId());
            model.setMember(request.getMember());
            model.setGroup(request.getGroup());
            model.setJoinedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public GroupMemberModel kickRequestToModel(GroupMemberModel model) {
        if (model != null) {
            model.setLeaveAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
