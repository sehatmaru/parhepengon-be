package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.enums.SplitTypeEnum;
import xcode.parhepengon.domain.mapper.BillMapper;
import xcode.parhepengon.domain.mapper.BillMemberMapper;
import xcode.parhepengon.domain.model.Bill;
import xcode.parhepengon.domain.model.BillMemberModel;
import xcode.parhepengon.domain.model.BillModel;
import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.repository.BillMemberRepository;
import xcode.parhepengon.domain.repository.BillRepository;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.bill.CreateBillRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.BillPresenter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static xcode.parhepengon.domain.enums.EventEnum.*;
import static xcode.parhepengon.domain.enums.SplitTypeEnum.*;
import static xcode.parhepengon.shared.ResponseCode.NOT_AUTHORIZED_MESSAGE;
import static xcode.parhepengon.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class BillService implements BillPresenter {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMemberRepository billMemberRepository;

    private final BillMapper billMapper = new BillMapper();
    private final BillMemberMapper billMemberMapper = new BillMemberMapper();

    @Override
    public BaseResponse<SecureIdResponse> create(CreateBillRequest request) {
        BaseResponse<SecureIdResponse> response = new BaseResponse<>();

        try {
            BillModel model = billMapper.createRequestToModel(request);
            billRepository.save(model);

            List<Bill> bills = calculateBills(request.getMember(), request.getAmount(), request.getMethod());

            bills.forEach(e -> billMemberRepository.save(billMemberMapper.setBill(model.getSecureId(), e)));

            historyService.addHistory(CREATE_BILL, null);

            response.setSuccess(new SecureIdResponse(model.getSecureId()));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> edit(CreateBillRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        BillModel model = checkBill(request.getSecureId());

        try {
            billRepository.save(billMapper.editModel(model, request));

            List<Bill> bills = calculateBills(request.getMember(), request.getAmount(), request.getMethod());

            bills.forEach(e -> {
                Optional<BillMemberModel> memberModel = billMemberRepository.findByBillAndMember(request.getSecureId(), e.getMember());

                if (memberModel.isEmpty()) {
                    throw new AppException(NOT_FOUND_MESSAGE);
                }

                billMemberRepository.save(billMemberMapper.updateBill(memberModel.get(), e));
            });

            historyService.addHistory(EDIT_BILL, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> delete(BaseRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        BillModel model = checkBill(request.getSecureId());

        try {
            billRepository.save(billMapper.deleteModel(model));

            List<BillMemberModel> memberModels = billMemberRepository.findAllByBill(request.getSecureId());
            memberModels.forEach(e -> e.setDeleted(true));

            billMemberRepository.saveAll(memberModels);

            historyService.addHistory(DELETE_BILL, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    private BillModel checkBill(String secureId) {
        Optional<BillModel> model = billRepository.findBySecureIdAndDeletedAtIsNull(secureId);

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        if (!Objects.equals(model.get().getCreatedBy(), CurrentUser.get().getUserSecureId())) {
            throw new AppException(NOT_AUTHORIZED_MESSAGE);
        }

        return model.get();
    }

    private List<Bill> calculateBills(List<Bill> bills, BigDecimal totalAmount, SplitTypeEnum method) {
        BigDecimal percent = BigDecimal.valueOf(100);

        if (method == EQUAL) {
            bills.forEach(e -> {
                e.setAmount(totalAmount.divide(BigDecimal.valueOf(bills.size()), 3, RoundingMode.DOWN));
                e.setAmountPercentage(e.getAmount().divide(totalAmount, 1, RoundingMode.DOWN).multiply(percent));
            });
        } else if (method == AMOUNT) {
            bills.forEach(e -> e.setAmountPercentage(e.getAmount().divide(totalAmount, 3, RoundingMode.DOWN).multiply(percent)));
        } else if (method == PERCENTAGE) {
            bills.forEach(e -> e.setAmount(e.getAmountPercentage()
                    .divide(percent)
                    .multiply(totalAmount)));
        }

        return bills;
    }
}
