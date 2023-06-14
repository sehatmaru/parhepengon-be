package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.BillModel;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<BillModel, String> {
    @Query(value = "SELECT * FROM t_bill" +
            " WHERE secure_id = :secureId AND deleted_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    Optional<BillModel> getBill(String secureId);
}
