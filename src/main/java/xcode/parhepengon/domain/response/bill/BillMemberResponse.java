package xcode.parhepengon.domain.response.bill;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BillMemberResponse {
    private String secureId;
    private String name;
    private BigDecimal amount;
    private BigDecimal amountPercentage;

}
