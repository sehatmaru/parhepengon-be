package xcode.parhepengon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_bill_member")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class BillMemberModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "bill_secure_id")
    private String bill;

    @Column(name = "member_secure_id")
    private String member;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "amount_percentage")
    private BigDecimal amountPercentage;

    @Column(name = "settle")
    private Boolean settle;

    @Column(name = "settle_at")
    private Date settleAt;

    @Column(name = "deleted")
    private Boolean deleted;
}
