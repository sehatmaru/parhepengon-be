package xcode.parhepengon.domain.request.bill;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.BillTypeEnum;
import xcode.parhepengon.domain.enums.SplitTypeEnum;
import xcode.parhepengon.domain.dto.Bill;
import xcode.parhepengon.domain.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateBillRequest extends BaseRequest {

    @NotBlank()
    private String title;

    private BillTypeEnum category;

    private SplitTypeEnum method;

    private String group;

    private BigDecimal amount;

    @NotBlank()
    private String prepaidBy;

    private List<Bill> member;

    public CreateBillRequest() {
    }
}
