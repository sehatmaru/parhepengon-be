package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.request.setting.SettingChangeRequest;
import xcode.parhepengon.domain.response.BaseResponse;

public interface SettingPresenter {
    BaseResponse<Boolean> change(SettingChangeRequest request);
}
