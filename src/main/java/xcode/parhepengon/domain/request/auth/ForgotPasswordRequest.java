package xcode.parhepengon.domain.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ForgotPasswordRequest {

    @NotBlank()
    private String email;

    public ForgotPasswordRequest() {
    }
}
