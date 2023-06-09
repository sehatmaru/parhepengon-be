package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;

public interface GroupPresenter {
    BaseResponse<SecureIdResponse> create(CreateGroupRequest request);
    BaseResponse<Boolean> edit(CreateGroupRequest request);
    BaseResponse<Boolean> delete(BaseRequest request);
}
