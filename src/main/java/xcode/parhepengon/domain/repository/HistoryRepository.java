package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.HistoryModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryModel, String> {
    Optional<HistoryModel> findBySecureId(String secureId);

    Optional<List<HistoryModel>> findByUserSecureIdOrderByCreatedAtDesc(String secureId);

}
