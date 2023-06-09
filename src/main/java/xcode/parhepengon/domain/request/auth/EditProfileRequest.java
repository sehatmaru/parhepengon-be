package xcode.parhepengon.domain.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EditProfileRequest {
    @NotBlank()
    private String fullname;

    @NotBlank()
    private String email;

    public EditProfileRequest() {
    }
}
