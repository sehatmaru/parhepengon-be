package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.dto.Bill;
import xcode.parhepengon.domain.model.BillMemberModel;

import java.util.Date;
import java.util.Objects;

import static xcode.parhepengon.shared.Utils.generateSecureId;

public class BillMemberMapper {
    public BillMemberModel setBill(String bill, Bill bills, String prepaidBy) {
        if (bills != null && !Objects.equals(bill, "")) {
            BillMemberModel model = new BillMemberModel();
            model.setSecureId(generateSecureId());
            model.setBill(bill);
            model.setMember(bills.getMember());
            model.setAmount(bills.getAmount());
            model.setAmountPercentage(bills.getAmountPercentage());

            if (prepaidBy.equals(bills.getMember())) {
                model.setSettle(true);
                model.setSettleAt(new Date());
            }

            return model;
        } else {
            return null;
        }
    }

    public BillMemberModel updateBill(BillMemberModel bill, Bill newBill, String prepaidBy) {
        if (bill != null) {
            bill.setAmount(newBill.getAmount());
            bill.setAmountPercentage(newBill.getAmountPercentage());

            if (prepaidBy.equals(newBill.getMember())) {
                bill.setSettle(true);
                bill.setSettleAt(new Date());
            }

            return bill;
        } else {
            return null;
        }
    }

}
