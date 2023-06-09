package xcode.parhepengon.domain.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank()
    private String code;
    @NotBlank()
    private String newPassword;

    public ResetPasswordRequest() {
    }
}
