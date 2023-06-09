package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.ResetModel;

import java.util.List;

@Repository
public interface ResetRepository extends JpaRepository<ResetModel, String> {
    List<ResetModel> findByCodeAndVerifiedIsFalse(String code);

}
