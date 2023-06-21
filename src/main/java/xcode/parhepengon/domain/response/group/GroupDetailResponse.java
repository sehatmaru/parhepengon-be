package xcode.parhepengon.domain.response.group;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.GroupTypeEnum;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class GroupDetailResponse {
    private String secureId;
    private Date createdAt;
    private Date lastUpdated;
    private String createdBy;
    private GroupTypeEnum category;
    private String name;
    private List<GroupHistoryResponse> history;
    private List<GroupMemberResponse> member;
    private List<GroupBillResponse> bills;
}
