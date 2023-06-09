package xcode.parhepengon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.parhepengon.domain.enums.EventEnum;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_history")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class HistoryModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "user_secure_id")
    private String userSecureId;

    @Column(name = "event")
    private EventEnum event;

    @Column(name = "username")
    private String username;

    @Column(name = "created_at")
    private Date createdAt;
}
