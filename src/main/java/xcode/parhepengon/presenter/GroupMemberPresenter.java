package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.request.group.AddKickMemberRequest;
import xcode.parhepengon.domain.response.BaseResponse;

public interface GroupMemberPresenter {
    BaseResponse<Boolean> add(AddKickMemberRequest request);
    BaseResponse<Boolean> kick(AddKickMemberRequest request);
}
