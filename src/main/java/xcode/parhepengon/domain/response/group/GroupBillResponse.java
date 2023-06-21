package xcode.parhepengon.domain.response.group;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GroupBillResponse {
    private String secureId;
    private String title;
    private BigDecimal amount;
    private String prepaidBy;
    private boolean settle;

}
