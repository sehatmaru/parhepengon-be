package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.GroupModel;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupModel, String> {

    @Query(value = "SELECT * FROM t_group" +
            " WHERE secure_id = :secureId AND deleted_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    Optional<GroupModel> getGroup(String secureId);
}
