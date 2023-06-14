package xcode.parhepengon.domain.response.group;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MemberResponse {
    private String secureId;
    private Date joinedAt;
    private String fullName;
}
