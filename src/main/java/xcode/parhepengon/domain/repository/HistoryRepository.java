package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.HistoryModel;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryModel, String> {
}
