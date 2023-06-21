package xcode.parhepengon.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.parhepengon.domain.request.setting.SettingChangeRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.setting.SettingsResponse;
import xcode.parhepengon.presenter.SettingPresenter;

@Validated
@RestController
@RequestMapping(value = "setting")
public class SettingAPI {
    
    final SettingPresenter settingPresenter;

    public SettingAPI(SettingPresenter settingPresenter) {
        this.settingPresenter = settingPresenter;
    }

    @PostMapping("/change")
    ResponseEntity<BaseResponse<Boolean>> change(@RequestBody @Validated SettingChangeRequest request) {
        BaseResponse<Boolean> response = settingPresenter.change(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/get")
    ResponseEntity<BaseResponse<SettingsResponse>> get() {
        BaseResponse<SettingsResponse> response = settingPresenter.getAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
