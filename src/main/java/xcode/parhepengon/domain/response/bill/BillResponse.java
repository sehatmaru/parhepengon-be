package xcode.parhepengon.domain.response.bill;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.BillTypeEnum;

import java.math.BigDecimal;

@Getter
@Setter
public class BillResponse {
    private String secureId;
    private BigDecimal totalAmount;
    private BillTypeEnum category;
    private boolean settle;
    private String title;
}
