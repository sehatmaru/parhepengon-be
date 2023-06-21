package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.BillHistoryModel;

import java.util.List;

@Repository
public interface BillHistoryRepository extends JpaRepository<BillHistoryModel, String> {

    @Query(value = "SELECT * FROM t_bill_history" +
            " WHERE bill_secure_id = :bill " +
            " LIMIT 1", nativeQuery = true)
    List<BillHistoryModel> getBillHistory(String bill);
}
