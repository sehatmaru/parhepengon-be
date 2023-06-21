package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.GroupHistoryModel;

@Repository
public interface GroupHistoryRepository extends JpaRepository<GroupHistoryModel, String> {

}
