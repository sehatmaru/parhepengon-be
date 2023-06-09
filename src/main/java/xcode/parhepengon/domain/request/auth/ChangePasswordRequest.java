package xcode.parhepengon.domain.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank()
    private String oldPassword;

    @NotBlank()
    private String newPassword;

    public ChangePasswordRequest() {
    }
}
