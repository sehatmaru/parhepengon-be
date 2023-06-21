package xcode.parhepengon.domain.dto;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.BillHistoryEventEnum;
import xcode.parhepengon.domain.enums.BillTypeEnum;
import xcode.parhepengon.domain.enums.SplitTypeEnum;

import java.math.BigDecimal;

@Setter
@Getter
public class BillUpdate {
    private String fullName;
    private String billSecureId;
    private String oldTitle;
    private String newTitle;
    private BillHistoryEventEnum event;
    private BigDecimal oldTotalAmount;
    private BillTypeEnum oldCategory;
    private SplitTypeEnum oldMethod;
    private boolean oldSettle;
    private BigDecimal newTotalAmount;
    private BillTypeEnum newCategory;
    private SplitTypeEnum newMethod;
    private boolean newSettle;
}
