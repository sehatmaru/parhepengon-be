package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.OtpModel;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpModel, String> {

    @Query(value = "SELECT * FROM t_otp" +
            " WHERE user_secure_id = :secureId AND verified IS FALSE" +
            " LIMIT 1", nativeQuery = true)
    Optional<OtpModel> getOtp(String secureId);

    @Query(value = "SELECT * FROM t_otp" +
            " WHERE user_secure_id = :secureId AND verified IS FALSE", nativeQuery = true)
    Optional<OtpModel> getUnverifiedOtp(String secureId);

}
