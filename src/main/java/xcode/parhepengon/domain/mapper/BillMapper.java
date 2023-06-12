package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.model.BillModel;
import xcode.parhepengon.domain.request.bill.CreateBillRequest;

import java.math.BigDecimal;
import java.util.Date;

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
            model.setPaidAmount(BigDecimal.ZERO);
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

}
