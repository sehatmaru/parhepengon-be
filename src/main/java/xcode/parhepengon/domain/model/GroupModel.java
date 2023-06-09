package xcode.parhepengon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.parhepengon.domain.enums.GroupTypeEnum;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_group")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class GroupModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "owner_secure_id")
    private String owner;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private GroupTypeEnum category;

    @Column(name = "created_at")
    private Date createdAt;
 
    @Column(name = "updated_at")
    private Date updatedAt;
 
    @Column(name = "deleted_at")
    private Date deletedAt;
}
