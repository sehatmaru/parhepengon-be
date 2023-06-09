package xcode.parhepengon.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecureIdResponse {
    private String secureId;

    public SecureIdResponse(String secureId) {
        this.secureId = secureId;
    }
}
