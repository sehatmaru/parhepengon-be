package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.dto.BillUpdate;
import xcode.parhepengon.domain.enums.SplitTypeEnum;
import xcode.parhepengon.domain.mapper.BillMapper;
import xcode.parhepengon.domain.mapper.BillMemberMapper;
import xcode.parhepengon.domain.dto.Bill;
import xcode.parhepengon.domain.model.BillMemberModel;
import xcode.parhepengon.domain.model.BillModel;
import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.repository.BillMemberRepository;
import xcode.parhepengon.domain.repository.BillRepository;
import xcode.parhepengon.domain.repository.GroupRepository;
import xcode.parhepengon.domain.repository.UserRepository;
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

import static xcode.parhepengon.domain.enums.BillHistoryEventEnum.*;
import static xcode.parhepengon.domain.enums.SplitTypeEnum.*;
import static xcode.parhepengon.shared.ResponseCode.*;

@Service
public class BillService implements BillPresenter {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMemberRepository billMemberRepository;

    @Autowired
    private GroupRepository groupRepository;

    private final BillMapper billMapper = new BillMapper();
    private final BillMemberMapper billMemberMapper = new BillMemberMapper();

    @Override
    public BaseResponse<SecureIdResponse> create(CreateBillRequest request) {
        BaseResponse<SecureIdResponse> response = new BaseResponse<>();

        if (!request.getGroup().isEmpty() && groupRepository.getGroup(request.getGroup()).isEmpty()) {
            throw new AppException(GROUP_NOT_FOUND_MESSAGE);
        }

        if (!isAllBillMemberExist(request.getMember())) {
            throw new AppException(MEMBER_NOT_FOUND_MESSAGE);
        }

        try {
            BillModel model = billMapper.createRequestToModel(request);
            billRepository.save(model);

            List<Bill> bills = calculateBills(request.getMember(), request.getAmount(), request.getMethod());

            bills.forEach(e -> billMemberRepository.save(billMemberMapper.setBill(model.getSecureId(), e)));

            historyService.addBillHistory(billMapper.createBillHistory(ADD_BILL, null, model.getSecureId(), profileService.getUserFullName()));

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
        BillUpdate billUpdate = billMapper.createBillHistory(UPDATE_BILL, model, model.getSecureId(), profileService.getUserFullName());

        if (!request.getGroup().isEmpty() && groupRepository.getGroup(request.getGroup()).isEmpty()) {
            throw new AppException(GROUP_NOT_FOUND_MESSAGE);
        }

        if (!isAllBillMemberExist(request.getMember())) {
            throw new AppException(MEMBER_NOT_FOUND_MESSAGE);
        }

        try {
            BillModel updated = billMapper.editModel(model, request);
            billUpdate = billMapper.setNewBill(updated, billUpdate);

            billRepository.save(updated);

            List<Bill> bills = calculateBills(request.getMember(), request.getAmount(), request.getMethod());

            bills.forEach(e -> {
                Optional<BillMemberModel> memberModel = billMemberRepository.getBillMember(request.getSecureId(), e.getMember());

                if (memberModel.isEmpty()) {
                    throw new AppException(NOT_FOUND_MESSAGE);
                }

                billMemberRepository.save(billMemberMapper.updateBill(memberModel.get(), e));
            });

            historyService.addBillHistory(billUpdate);

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

            List<BillMemberModel> memberModels = billMemberRepository.getBillMemberList(request.getSecureId());
            memberModels.forEach(e -> e.setDeleted(true));

            billMemberRepository.saveAll(memberModels);

            historyService.addBillHistory(billMapper.createBillHistory(DELETE_BILL, null, "", profileService.getUserFullName()));

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    private BillModel checkBill(String secureId) {
        Optional<BillModel> model = billRepository.getBill(secureId);

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

    public boolean isAllBillMemberExist(List<Bill> bills) {
        boolean result = true;

        for (Bill b : bills) {
            if (userRepository.getActiveUserBySecureId(b.getMember()).isEmpty()) {
                result = false;
                break;
            }
        }

        return result;
    }
}
