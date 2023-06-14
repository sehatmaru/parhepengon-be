package xcode.parhepengon.domain.request.setting;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.SettingEnum;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SettingChangeRequest {
    private SettingEnum type;

    @NotBlank()
    private String value;

    public SettingChangeRequest() {
    }
}
