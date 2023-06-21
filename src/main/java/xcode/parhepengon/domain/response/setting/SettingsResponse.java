package xcode.parhepengon.domain.response.setting;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.CurrencyEnum;
import xcode.parhepengon.domain.enums.LanguageEnum;

import java.util.Date;

@Getter
@Setter
public class SettingsResponse {
    private LanguageEnum language = LanguageEnum.ID;
    private CurrencyEnum currency = CurrencyEnum.IDR;
    private Date updatedAt;

}
