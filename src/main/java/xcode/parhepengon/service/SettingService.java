package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.mapper.SettingMapper;
import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.model.SettingModel;
import xcode.parhepengon.domain.repository.SettingRepository;
import xcode.parhepengon.domain.request.setting.SettingChangeRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.SettingPresenter;

import java.util.Optional;

import static xcode.parhepengon.domain.enums.EventEnum.UPDATE_SETTING;
import static xcode.parhepengon.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class SettingService implements SettingPresenter {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private SettingRepository settingRepository;


    private final SettingMapper settingMapper = new SettingMapper();

    @Override
    public BaseResponse<Boolean> change(SettingChangeRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<SettingModel> model = settingRepository.findByUser(CurrentUser.get().getUserSecureId());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            settingRepository.save(settingMapper.updateModel(model.get(), request));

            historyService.addHistory(UPDATE_SETTING, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }
}
