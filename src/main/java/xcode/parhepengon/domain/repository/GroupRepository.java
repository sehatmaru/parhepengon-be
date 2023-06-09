package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.GroupModel;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupModel, String> {
    Optional<GroupModel> findBySecureIdAndDeletedAtIsNull(String secureId);
}
