package xcode.parhepengon.domain.request.group;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.GroupTypeEnum;
import xcode.parhepengon.domain.request.BaseRequest;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateGroupRequest extends BaseRequest {

    @NotBlank()
    private String groupName;

    private GroupTypeEnum category;

    public CreateGroupRequest() {
    }
}
