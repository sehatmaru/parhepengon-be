package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.OtpModel;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpModel, String> {
    Optional<OtpModel> findByUserAndVerifiedIsFalse(String secureId);

    boolean existsByUserAndVerifiedIsFalse(String secureId);

}
