package xcode.parhepengon.domain.response.group;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GroupMemberResponse {
    private String secureId;
    private String name;
    private Date joinedAt;
    private Date leaveAt;

}
