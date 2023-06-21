package xcode.parhepengon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.parhepengon.domain.enums.GroupHistoryEventEnum;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_group_history")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class GroupHistoryModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "group_secure_id")
    private String group;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "comment")
    private String comment;

    @Column(name = "event")
    private GroupHistoryEventEnum event;

    @Column(name = "created_at")
    private Date createdAt;

}
