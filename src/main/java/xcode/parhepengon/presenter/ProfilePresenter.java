package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.request.profile.EditProfileRequest;
import xcode.parhepengon.domain.response.BaseResponse;

public interface ProfilePresenter {
    BaseResponse<Boolean> update(EditProfileRequest request);
}
