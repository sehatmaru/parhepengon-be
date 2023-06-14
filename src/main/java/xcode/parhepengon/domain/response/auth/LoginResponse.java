package xcode.parhepengon.domain.response.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String secureId;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String accessToken;
}
