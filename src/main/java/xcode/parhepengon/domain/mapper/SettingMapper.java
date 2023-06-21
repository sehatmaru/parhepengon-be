package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.enums.CurrencyEnum;
import xcode.parhepengon.domain.enums.LanguageEnum;
import xcode.parhepengon.domain.model.SettingModel;
import xcode.parhepengon.domain.request.setting.SettingChangeRequest;
import xcode.parhepengon.domain.response.setting.SettingsResponse;

import java.util.Date;

import static xcode.parhepengon.domain.enums.SettingEnum.CURRENCY;
import static xcode.parhepengon.domain.enums.SettingEnum.LANGUAGE;

public class SettingMapper {
    public SettingModel updateModel(SettingModel model, SettingChangeRequest request) {
        if (model != null && request != null) {
            if (request.getType() == CURRENCY) {
                model.setCurrency(CurrencyEnum.valueOf(request.getValue()));
            } else if (request.getType() == LANGUAGE) {
                model.setLanguage(LanguageEnum.valueOf(request.getValue()));
            }

            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public SettingsResponse modelToResponse(SettingModel model) {
        if (model != null) {
            SettingsResponse response = new SettingsResponse();
            response.setCurrency(model.getCurrency());
            response.setLanguage(model.getLanguage());
            response.setUpdatedAt(model.getUpdatedAt());

            return response;
        } else {
            return null;
        }
    }

}
