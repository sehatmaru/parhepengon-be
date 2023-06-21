package xcode.parhepengon.domain.response.bill;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.BillTypeEnum;
import xcode.parhepengon.domain.enums.SplitTypeEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BillDetailResponse {
    private String secureId;
    private Date createdAt;
    private Date lastUpdated;
    private String createdBy;
    private BigDecimal totalAmount;
    private BillTypeEnum category;
    private SplitTypeEnum method;
    private boolean settle;
    private Date settleAt;
    private String title;
    private List<BillHistoryResponse> history;
    private List<BillMemberResponse> member;
}
