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
@Table(name = "t_group_member")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "group_secure_id")
    private String group;

    @Column(name = "member_secure_id")
    private String member;

    @Column(name = "joined_at")
    private Date joinedAt;
 
    @Column(name = "leave_at")
    private Date leaveAt;
}
