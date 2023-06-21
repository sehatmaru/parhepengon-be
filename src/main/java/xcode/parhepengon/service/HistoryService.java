package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.dto.BillUpdate;
import xcode.parhepengon.domain.dto.GroupUpdate;
import xcode.parhepengon.domain.enums.AccountHistoryEventEnum;
import xcode.parhepengon.domain.mapper.HistoryMapper;
import xcode.parhepengon.domain.model.BillHistoryModel;
import xcode.parhepengon.domain.model.BillModel;
import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.repository.AccountHistoryRepository;
import xcode.parhepengon.domain.repository.BillHistoryRepository;
import xcode.parhepengon.domain.repository.BillRepository;
import xcode.parhepengon.domain.repository.GroupHistoryRepository;
import xcode.parhepengon.domain.request.comment.AddCommentRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.HistoryPresenter;

import java.util.Optional;

import static xcode.parhepengon.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class HistoryService implements HistoryPresenter {

    @Autowired
    AccountHistoryRepository accountHistoryRepository;

    @Autowired
    BillHistoryRepository billHistoryRepository;

    @Autowired
    GroupHistoryRepository groupHistoryRepository;

    @Autowired
    BillRepository billRepository;

    private final HistoryMapper historyMapper = new HistoryMapper();

    @Override
    public BaseResponse<SecureIdResponse> addComment(AddCommentRequest request) {
        BaseResponse<SecureIdResponse> response = new BaseResponse<>();

        Optional<BillModel> model = billRepository.getBill(request.getBill());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            BillHistoryModel historyModel = historyMapper.addCommentMapper(request, model.get().getSecureId());
            billHistoryRepository.save(historyModel);

            response.setSuccess(new SecureIdResponse(historyModel.getSecureId()));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public void addAccountHistory(AccountHistoryEventEnum event, String secureId) {
        String userSecureId = secureId != null ? secureId : CurrentUser.get().getUserSecureId();

        try {
            accountHistoryRepository.save(historyMapper.accountHistoryMapper(event, userSecureId));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }
    }

    public void addBillHistory(BillUpdate bill) {
        try {
            billHistoryRepository.save(historyMapper.billHistoryMapper(bill));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }
    }

    public void addGroupHistory(GroupUpdate group) {
        try {
            groupHistoryRepository.save(historyMapper.groupHistoryMapper(group));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }
    }
}
