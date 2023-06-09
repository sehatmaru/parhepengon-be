package xcode.parhepengon.domain.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class VerifyOtpRequest {
    @NotBlank()
    private String otp;

    public VerifyOtpRequest() {
    }
}
