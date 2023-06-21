package xcode.parhepengon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.parhepengon.domain.enums.BillHistoryEventEnum;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_bill_history")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class BillHistoryModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "bill_secure_id")
    private String bill;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "comment")
    private String comment;

    @Column(name = "event")
    private BillHistoryEventEnum event;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
