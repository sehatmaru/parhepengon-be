package xcode.parhepengon.domain.response.bill;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.BillHistoryEventEnum;

import java.util.Date;

@Getter
@Setter
public class BillHistoryResponse {
    private BillHistoryEventEnum event;
    private Date createdAt;
    private String createdBy;
    private String comment;

}
