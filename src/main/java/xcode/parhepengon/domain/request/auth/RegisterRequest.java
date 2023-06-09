package xcode.parhepengon.domain.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank()
    private String fullname;

    @NotBlank()
    private String username;

    @NotBlank()
    private String email;

    @NotBlank()
    private String password;

    public RegisterRequest() {
    }
}
