package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.enums.EventEnum;
import xcode.parhepengon.domain.mapper.HistoryMapper;
import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.model.HistoryModel;
import xcode.parhepengon.domain.repository.HistoryRepository;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.HistoryPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static xcode.parhepengon.shared.Utils.generateSecureId;

@Service
public class HistoryService implements HistoryPresenter {

    @Autowired
    HistoryRepository historyRepository;

    private final HistoryMapper historyMapper = new HistoryMapper();

    public void addHistory(EventEnum event, String secureId) {
        String userSecureId = secureId != null ? secureId : CurrentUser.get().getUserSecureId();
        try {
            HistoryModel model = new HistoryModel();
            model.setSecureId(generateSecureId());
            model.setUserSecureId(userSecureId);
            model.setCreatedAt(new Date());
            model.setEvent(event);

            historyRepository.save(model);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }
    }
}
