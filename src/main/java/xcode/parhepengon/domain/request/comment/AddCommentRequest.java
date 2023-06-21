package xcode.parhepengon.domain.request.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AddCommentRequest {

    @NotBlank()
    private String bill;

    @NotBlank()
    private String value;

    public AddCommentRequest() {
    }
}
