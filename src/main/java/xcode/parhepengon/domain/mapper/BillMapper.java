package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.dto.BillUpdate;
import xcode.parhepengon.domain.enums.BillHistoryEventEnum;
import xcode.parhepengon.domain.model.*;
import xcode.parhepengon.domain.request.bill.CreateBillRequest;
import xcode.parhepengon.domain.response.bill.BillDetailResponse;
import xcode.parhepengon.domain.response.bill.BillHistoryResponse;
import xcode.parhepengon.domain.response.bill.BillMemberResponse;
import xcode.parhepengon.domain.response.bill.BillResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.parhepengon.shared.Utils.generateSecureId;

public class BillMapper {
    public BillModel createRequestToModel(CreateBillRequest request) {
        if (request != null) {
            BillModel model = new BillModel();
            model.setSecureId(generateSecureId());
            model.setTitle(request.getTitle());
            model.setCategory(request.getCategory());
            model.setCreatedBy(CurrentUser.get().getUserSecureId());
            model.setMethod(request.getMethod());
            model.setTotalAmount(request.getAmount());
            model.setGroup(request.getGroup());
            model.setPrepaidBy(request.getPrepaidBy());
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public BillModel editModel(BillModel model, CreateBillRequest request) {
        if (model != null && request != null) {
            model.setTitle(request.getTitle());
            model.setCategory(request.getCategory());
            model.setMethod(request.getMethod());
            model.setTotalAmount(request.getAmount());
            model.setGroup(request.getGroup());
            model.setPrepaidBy(request.getPrepaidBy());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public BillModel deleteModel(BillModel model) {
        if (model != null) {
            model.setDeletedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public BillUpdate createBillHistory(BillHistoryEventEnum event, BillModel oldBill, String billSecureId, String fullName) {
        BillUpdate model = new BillUpdate();
        model.setBillSecureId(billSecureId);
        model.setFullName(fullName);
        model.setEvent(event);

        if (oldBill != null) {
            return setOldBill(oldBill, model);
        } else {
            return model;
        }
    }

    public BillUpdate setOldBill(BillModel bill, BillUpdate billUpdate) {
        if (bill != null && billUpdate != null) {
            billUpdate.setOldCategory(bill.getCategory());
            billUpdate.setOldSettle(bill.isSettle());
            billUpdate.setOldMethod(bill.getMethod());
            billUpdate.setOldTitle(bill.getTitle());
            billUpdate.setOldTotalAmount(bill.getTotalAmount());

            return billUpdate;
        } else {
            return null;
        }
    }

    public BillUpdate setNewBill(BillModel bill, BillUpdate billUpdate) {
        if (bill != null && billUpdate != null) {
            billUpdate.setNewCategory(bill.getCategory());
            billUpdate.setNewSettle(bill.isSettle());
            billUpdate.setNewMethod(bill.getMethod());
            billUpdate.setNewTitle(bill.getTitle());
            billUpdate.setNewTotalAmount(bill.getTotalAmount());

            return billUpdate;
        } else {
            return null;
        }
    }

    public BillDetailResponse generateBillDetailResponse(BillModel billModel, List<BillMemberModel> billMemberModels, List<BillHistoryModel> billHistoryModels) {
        BillDetailResponse model = new BillDetailResponse();

        if (billModel != null) {
            model.setSecureId(billModel.getSecureId());
            model.setTitle(billModel.getTitle());
            model.setCategory(billModel.getCategory());
            model.setMethod(billModel.getMethod());
            model.setTotalAmount(billModel.getTotalAmount());
            model.setCreatedAt(billModel.getCreatedAt());
            model.setLastUpdated(billModel.getUpdatedAt());
            model.setSettle(billModel.isSettle());
            model.setSettleAt(billModel.getSettleAt());
            model.setHistory(generateBillHistoryResponse(billHistoryModels));
            model.setMember(generateBillMemberResponse(billMemberModels));
        }

        return model;
    }

    public List<BillHistoryResponse> generateBillHistoryResponse(List<BillHistoryModel> bills) {
        List<BillHistoryResponse> result = new ArrayList<>();

        if (bills != null && !bills.isEmpty()) {
            for (BillHistoryModel bill : bills) {
                BillHistoryResponse response = new BillHistoryResponse();
                response.setEvent(bill.getEvent());
                response.setComment(bill.getComment());
                response.setCreatedAt(bill.getCreatedAt());

                result.add(response);
            }
        }

        return result;
    }

    public List<BillMemberResponse> generateBillMemberResponse(List<BillMemberModel> bills) {
        List<BillMemberResponse> result = new ArrayList<>();

        if (bills != null && !bills.isEmpty()) {
            for (BillMemberModel bill : bills) {
                BillMemberResponse response = new BillMemberResponse();
                response.setAmount(bill.getAmount());
                response.setSecureId(bill.getSecureId());
                response.setAmountPercentage(bill.getAmountPercentage());

                result.add(response);
            }
        }

        return result;
    }

    public List<BillResponse> generateBillResponse(List<BillModel> billModels) {
        List<BillResponse> result = new ArrayList<>();

        if (billModels != null && !billModels.isEmpty()) {
            for (BillModel bill : billModels) {
                BillResponse response = new BillResponse();
                response.setSecureId(bill.getSecureId());
                response.setTitle(bill.getTitle());
                response.setCategory(bill.getCategory());
                response.setSettle(bill.isSettle());
                response.setTotalAmount(bill.getTotalAmount());

                result.add(response);
            }
        }

        return result;
    }
}
