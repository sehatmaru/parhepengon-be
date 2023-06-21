package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.GroupMemberModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMemberModel, String> {

    @Query(value = "SELECT * FROM t_group_member" +
            " WHERE group_secure_id = :group AND member_secure_id = :member" +
            " AND leave_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    Optional<GroupMemberModel> getGroupMember(String group, String member);

    @Query(value = "SELECT * FROM t_group_member" +
            " WHERE group_secure_id = :secureId AND leave_at IS NULL", nativeQuery = true)
    List<GroupMemberModel> getGroupMemberList(String secureId);

    @Query(value = "SELECT * FROM t_group_member" +
            " WHERE group_secure_id = :secureId AND member_secure_id != :user" +
            " AND leave_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    GroupMemberModel getNewOwner(String secureId, String user);

    @Query(value = "SELECT COUNT(*) FROM t_group_member" +
            " WHERE group_secure_id = :secureId AND leave_at IS NULL", nativeQuery = true)
    int countGroupMember(String secureId);
}
