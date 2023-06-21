package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.GroupHistoryModel;

import java.util.List;

@Repository
public interface GroupHistoryRepository extends JpaRepository<GroupHistoryModel, String> {

    @Query(value = "SELECT * FROM t_group_history" +
            " WHERE group_secure_id = :group " +
            " LIMIT 1", nativeQuery = true)
    List<GroupHistoryModel> getGroupHistory(String group);
}
