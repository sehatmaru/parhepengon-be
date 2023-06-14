package xcode.parhepengon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.parhepengon.domain.enums.CurrencyEnum;
import xcode.parhepengon.domain.enums.LanguageEnum;

import javax.persistence.*;
import java.util.Date;

import static xcode.parhepengon.shared.Utils.generateSecureId;

@Data
@Builder
@Entity
@Table(name = "t_setting")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class SettingModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "user_secure_id")
    private String user;

    @Column(name = "language")
    private LanguageEnum language = LanguageEnum.ID;

    @Column(name = "currency")
    private CurrencyEnum currency = CurrencyEnum.IDR;

    @Column(name = "updated_at")
    private Date updatedAt;

    public SettingModel(String user) {
        this.secureId = generateSecureId();
        this.user = user;
    }

}
