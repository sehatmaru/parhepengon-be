package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.ProfileModel;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileModel, String> {
    Optional<ProfileModel> findByUserAndDeletedAtIsNull(String user);
    Optional<ProfileModel> findByEmailAndDeletedAtIsNull(String email);
}
