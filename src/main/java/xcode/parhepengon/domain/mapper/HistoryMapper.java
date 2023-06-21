package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.dto.BillUpdate;
import xcode.parhepengon.domain.dto.GroupUpdate;
import xcode.parhepengon.domain.enums.AccountHistoryEventEnum;
import xcode.parhepengon.domain.model.AccountHistoryModel;
import xcode.parhepengon.domain.model.BillHistoryModel;
import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.model.GroupHistoryModel;
import xcode.parhepengon.domain.request.comment.AddCommentRequest;

import java.util.Date;
import java.util.Objects;

import static xcode.parhepengon.domain.enums.BillHistoryEventEnum.*;
import static xcode.parhepengon.domain.enums.GroupHistoryEventEnum.*;
import static xcode.parhepengon.shared.Utils.generateSecureId;

public class HistoryMapper {

    public AccountHistoryModel accountHistoryMapper(AccountHistoryEventEnum event, String user) {
        if (event != null) {
            AccountHistoryModel model = new AccountHistoryModel();
            model.setSecureId(generateSecureId());
            model.setUser(user);
            model.setEvent(event);
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public BillHistoryModel billHistoryMapper(BillUpdate bill) {
        if (bill != null) {
            BillHistoryModel model = new BillHistoryModel();
            model.setSecureId(generateSecureId());
            model.setCreatedBy(CurrentUser.get().getUserSecureId());
            model.setEvent(bill.getEvent());
            model.setBill(bill.getBillSecureId());
            model.setComment(completeBillComment(bill));
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public BillHistoryModel addCommentMapper(AddCommentRequest request, String billSecureId) {
        if (request != null) {
            BillHistoryModel model = new BillHistoryModel();
            model.setSecureId(generateSecureId());
            model.setCreatedBy(CurrentUser.get().getUserSecureId());
            model.setEvent(ADD_COMMENT);
            model.setComment(request.getValue());
            model.setBill(billSecureId);
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public GroupHistoryModel groupHistoryMapper(GroupUpdate group) {
        if (group != null) {
            GroupHistoryModel model = new GroupHistoryModel();
            model.setSecureId(generateSecureId());
            model.setCreatedBy(CurrentUser.get().getUserSecureId());
            model.setEvent(group.getEvent());
            model.setGroup(group.getGroupSecureId());
            model.setComment(completeGroupComment(group));
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    private String completeBillComment(BillUpdate bill) {
        String result = bill.getFullName();

        if (bill.getEvent() == ADD_BILL) {
            result += " menambahkan tagihan.";
        } else if (bill.getEvent() == UPDATE_BILL) {
            result += " mengubah ";

            if (!Objects.equals(bill.getOldTotalAmount(), bill.getNewTotalAmount())) {
                result += "Jumlah Pembayaran menjadi " + bill.getNewTotalAmount()
                        + " dan Metode Pembagian menjadi " + bill.getNewMethod()
                        + ". Harap perhatikan perubahan jumlah yang harus di bayar.";
            }

            if (!Objects.equals(bill.getOldCategory(), bill.getNewCategory())) {
                result += "Kategori menjadi " + bill.getNewCategory().name() + ".";
            }

            if (!Objects.equals(bill.getOldSettle(), bill.getNewSettle())) {
                result += "tagihan menjadi ";
                result += Boolean.TRUE.equals(bill.getNewSettle()) ? "belum selesai." : "sudah selesai.";
            }

            if (!Objects.equals(bill.getOldTitle(), bill.getNewTitle())) {
                result += " judul tagihan menjadi " + bill.getNewTitle();
            }
        } else if (bill.getEvent() == DELETE_BILL) {
            result += " menghapus tagihan.";
        }

        return result;
    }

    private String completeGroupComment(GroupUpdate group) {
        String result = group.getFullName();

        if (group.getEvent() == CREATE_GROUP) {
            result += " membuat grup.";
        } else if (group.getEvent() == EDIT_GROUP) {
            result += " mengubah grup.";
        } else if (group.getEvent() == DELETE_GROUP) {
            result += " menghapus grup.";
        } else if (group.getEvent() == ADD_MEMBER) {
            result += " menambahkan " + group.getAddedUserName() + " ke dalam grup.";
        } else if (group.getEvent() == KICK_MEMBER) {
            result += " mengeluarkan " + group.getDeletedUserName() + " dari grup.";
        } else if (group.getEvent() == LEAVE_GROUP) {
            result += " keluar dari grup.";
        } else if (group.getEvent() == CHANGE_OWNER) {
            result += " menunjuk " + group.getNewOwner() + " menjadi ketua grup.";
        }

        return result;
    }
}
