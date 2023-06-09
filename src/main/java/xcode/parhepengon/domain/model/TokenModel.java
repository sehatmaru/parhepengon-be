package xcode.parhepengon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

import static xcode.parhepengon.shared.Utils.*;
import static xcode.parhepengon.shared.Utils.getTomorrowDate;

@Data
@Builder
@Entity
@Table(name = "t_token")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class TokenModel {

    @Id
    @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "user_secure_id")
    private String userSecureId;

    @Column(name = "token")
    private String token;

    @Column(name = "temporary")
    private boolean temporary;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "expire_at")
    private Date expireAt;

    private String password;

    public TokenModel(String token, String userSecureId, boolean temporary) {
        this.secureId = generateSecureId();
        this.token = token;
        this.userSecureId = userSecureId;
        this.createdAt = new Date();
        this.expireAt = getTomorrowDate();
        this.temporary = temporary;
    }

    public boolean isValid() {
        return !expireAt.before(new Date());
    }
}
