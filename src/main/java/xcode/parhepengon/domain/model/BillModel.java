package xcode.parhepengon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.parhepengon.domain.enums.BillTypeEnum;
import xcode.parhepengon.domain.enums.SplitTypeEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_bill")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class BillModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "group_secure_id")
    private String group;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "title")
    private String title;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "unpaid_amount")
    private BigDecimal unpaidAmount;

    @Column(name = "prepaid_by")
    private String prepaidBy;

    @Column(name = "category")
    private BillTypeEnum category;

    @Column(name = "split_method")
    private SplitTypeEnum method;

    @Column(name = "settle")
    private boolean settle = false;

    @Column(name = "settle_at")
    private Date settleAt;

    @Column(name = "created_at")
    private Date createdAt;
 
    @Column(name = "updated_at")
    private Date updatedAt;
 
    @Column(name = "deleted_at")
    private Date deletedAt;
}
