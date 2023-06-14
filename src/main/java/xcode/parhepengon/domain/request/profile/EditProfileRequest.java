package xcode.parhepengon.domain.request.profile;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EditProfileRequest {
    @NotBlank()
    private String fullName;

    @NotBlank()
    private String username;

    @NotBlank()
    private String email;

    @NotBlank()
    private String phone;

    public EditProfileRequest() {
    }
}
