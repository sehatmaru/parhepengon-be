package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.BillModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<BillModel, String> {
    @Query(value = "SELECT * FROM t_bill" +
            " WHERE secure_id = :secureId" +
            " LIMIT 1", nativeQuery = true)
    Optional<BillModel> getBill(String secureId);

    @Query(value = "SELECT * FROM t_bill" +
            " WHERE group_secure_id = :group AND deleted_at IS NULL", nativeQuery = true)
    List<BillModel> getGroupBills(String group);
}
