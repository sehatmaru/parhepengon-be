package xcode.parhepengon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_reset")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class ResetModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "code")
    private String code;

    @Column(name = "email")
    private String email;

    @Column(name = "verified")
    private boolean verified;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "expire_at")
    private Date expireAt;

    public boolean isValid() {
        return !expireAt.before(new Date());
    }

}
