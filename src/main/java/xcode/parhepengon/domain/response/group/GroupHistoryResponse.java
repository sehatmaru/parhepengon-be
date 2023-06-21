package xcode.parhepengon.domain.response.group;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.GroupHistoryEventEnum;

import java.util.Date;

@Getter
@Setter
public class GroupHistoryResponse {
    private GroupHistoryEventEnum event;
    private Date createdAt;
    private String createdBy;
    private String comment;

}
