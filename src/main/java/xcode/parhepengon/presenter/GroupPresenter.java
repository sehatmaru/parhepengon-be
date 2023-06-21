package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.enums.GroupTypeEnum;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.domain.response.group.GroupDetailResponse;
import xcode.parhepengon.domain.response.group.GroupResponse;
import xcode.parhepengon.domain.response.group.MemberResponse;

import java.util.List;

public interface GroupPresenter {
    BaseResponse<SecureIdResponse> create(CreateGroupRequest request);
    BaseResponse<Boolean> edit(CreateGroupRequest request);
    BaseResponse<Boolean> delete(BaseRequest request);
    BaseResponse<List<MemberResponse>> getMemberList(BaseRequest request);
    BaseResponse<List<GroupResponse>> getList();
    BaseResponse<GroupDetailResponse> detail(BaseRequest request);
    BaseResponse<List<GroupTypeEnum>> getCategoryList();
}
