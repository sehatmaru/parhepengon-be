package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.dto.BillUpdate;
import xcode.parhepengon.domain.enums.BillTypeEnum;
import xcode.parhepengon.domain.enums.SplitTypeEnum;
import xcode.parhepengon.domain.mapper.BillMapper;
import xcode.parhepengon.domain.mapper.BillMemberMapper;
import xcode.parhepengon.domain.dto.Bill;
import xcode.parhepengon.domain.model.BillHistoryModel;
import xcode.parhepengon.domain.model.BillMemberModel;
import xcode.parhepengon.domain.model.BillModel;
import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.repository.*;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.bill.CreateBillRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.domain.response.bill.BillDetailResponse;
import xcode.parhepengon.domain.response.bill.BillResponse;
import xcode.parhepengon.exception.AppException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static xcode.parhepengon.domain.enums.BillHistoryEventEnum.*;
import static xcode.parhepengon.domain.enums.SplitTypeEnum.*;
import static xcode.parhepengon.shared.ResponseCode.*;

@Service
public class BillService {

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

    @Autowired
    private BillHistoryRepository billHistoryRepository;

    private final BillMapper billMapper = new BillMapper();
    private final BillMemberMapper billMemberMapper = new BillMemberMapper();

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

            List<Bill> bills = calculateBills(request.getMember(), request.getAmount(), request.getMethod());
            bills.forEach(e -> billMemberRepository.save(billMemberMapper.setBill(model.getSecureId(), e, model.getPrepaidBy())));

            for (Bill b : bills) {
                if (b.getMember().equals(model.getPrepaidBy())) {
                    model.setPaidAmount(b.getAmount());
                    model.setUnpaidAmount(model.getTotalAmount().subtract(model.getPaidAmount()));
                    break;
                }
            }

            billRepository.save(model);

            historyService.addBillHistory(billMapper.createBillHistory(ADD_BILL, null, model.getSecureId(), profileService.getUserFullName()));

            response.setSuccess(new SecureIdResponse(model.getSecureId()));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

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


            List<Bill> bills = calculateBills(request.getMember(), request.getAmount(), request.getMethod());

            bills.forEach(e -> {
                Optional<BillMemberModel> memberModel = billMemberRepository.getBillMember(request.getSecureId(), e.getMember());

                if (memberModel.isEmpty()) {
                    throw new AppException(NOT_FOUND_MESSAGE);
                }

                billMemberRepository.save(billMemberMapper.updateBill(memberModel.get(), e, updated.getPrepaidBy()));
            });

            for (Bill b : bills) {
                if (b.getMember().equals(updated.getPrepaidBy())) {
                    updated.setPaidAmount(b.getAmount());
                    updated.setUnpaidAmount(updated.getTotalAmount().subtract(updated.getPaidAmount()));
                    break;
                }
            }

            billRepository.save(updated);

            historyService.addBillHistory(billUpdate);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

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

    public BaseResponse<BillDetailResponse> detail(BaseRequest request) {
        BaseResponse<BillDetailResponse> response = new BaseResponse<>();

        Optional<BillModel> billModel = billRepository.getBill(request.getSecureId());

        if (billModel.isEmpty()) {
            throw new AppException(BILL_NOT_FOUND_MESSAGE);
        }

        try {
            List<BillHistoryModel> billHistoryModels = billHistoryRepository.getBillHistory(request.getSecureId());
            List<BillMemberModel> billMemberModels = billMemberRepository.getBillMemberList(request.getSecureId());

            BillDetailResponse bills = billMapper.generateBillDetailResponse(billModel.get(), billMemberModels, billHistoryModels);
            bills.setCreatedBy(profileService.getUserFullName(billModel.get().getCreatedBy()));

            for (int i=0; i<billHistoryModels.size(); i++) {
                bills.getHistory().get(i).setCreatedBy(profileService.getUserFullName(billHistoryModels.get(i).getCreatedBy()));
            }

            for (int i=0; i<billMemberModels.size(); i++) {
                bills.getMember().get(i).setName(profileService.getUserFullName(billMemberModels.get(i).getMember()));
            }

            response.setSuccess(bills);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public BaseResponse<List<BillResponse>> list() {
        BaseResponse<List<BillResponse>> response = new BaseResponse<>();

        List<BillMemberModel> billMemberModels = billMemberRepository.getBillByUser(CurrentUser.get().getUserSecureId());

        if (billMemberModels.isEmpty()) {
            response.setSuccess(Collections.emptyList());
        } else {
            try {
                List<BillModel> billModels = new ArrayList<>();
                billMemberModels.forEach(e -> {
                    BillModel bill = billRepository.getBill(e.getBill()).orElse(null);
                    billModels.add(bill);
                });

                response.setSuccess(billMapper.generateBillResponse(billModels));
            } catch (Exception e) {
                throw new AppException(e.toString());
            }
        }

        return response;
    }

    public BaseResponse<List<BillTypeEnum>> getCategoryList() {
        BaseResponse<List<BillTypeEnum>> response = new BaseResponse<>();
        List<BillTypeEnum> list = Arrays.asList(BillTypeEnum.values());

        response.setSuccess(list);

        return response;
    }

    public BaseResponse<List<SplitTypeEnum>> getMethodList() {
        BaseResponse<List<SplitTypeEnum>> response = new BaseResponse<>();
        List<SplitTypeEnum> list = Arrays.asList(SplitTypeEnum.values());

        response.setSuccess(list);

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
