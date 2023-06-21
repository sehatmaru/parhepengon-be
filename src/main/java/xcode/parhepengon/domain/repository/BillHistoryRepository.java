package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.BillHistoryModel;

@Repository
public interface BillHistoryRepository extends JpaRepository<BillHistoryModel, String> {

}
