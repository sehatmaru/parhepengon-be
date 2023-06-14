package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.ResetModel;

import java.util.List;

@Repository
public interface ResetRepository extends JpaRepository<ResetModel, String> {

    @Query(value = "SELECT * FROM t_reset" +
            " WHERE code = :code AND verified IS FALSE", nativeQuery = true)
    List<ResetModel> getReset(String code);

}
