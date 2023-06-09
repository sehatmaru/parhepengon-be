package xcode.parhepengon.domain.response.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String secureId;
    private String fullname;
    private String username;
    private String email;
    private String accessToken;
}
