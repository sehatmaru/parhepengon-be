package xcode.parhepengon.domain.request.group;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AddKickMemberRequest {

    @NotBlank()
    private String group;

    @NotBlank()
    private String member;

    public AddKickMemberRequest() {
    }
}
