package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.GroupModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupModel, String> {

    @Query(value = "SELECT * FROM t_group" +
            " WHERE secure_id = :secureId AND deleted_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    Optional<GroupModel> getGroup(String secureId);

    @Query(value = "SELECT * FROM t_group" +
            " WHERE owner_secure_id = :owner AND deleted_at IS NULL", nativeQuery = true)
    List<GroupModel> getOwnerGroups(String owner);
}
