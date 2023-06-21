package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.request.setting.SettingChangeRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.setting.SettingsResponse;

public interface SettingPresenter {
    BaseResponse<Boolean> change(SettingChangeRequest request);
    BaseResponse<SettingsResponse> getAll();
}
