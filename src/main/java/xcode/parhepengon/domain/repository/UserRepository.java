package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.UserModel;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByUsernameOrEmailAndActiveIsTrueAndDeletedAtIsNull(String username, String email);
    Optional<UserModel> findByUsernameAndActiveIsTrueAndDeletedAtIsNull(String username);
    Optional<UserModel> findByEmailAndActiveIsTrueAndDeletedAtIsNull(String email);
    Optional<UserModel> findBySecureIdAndDeletedAtIsNull(String secureId);
    Optional<UserModel> findBySecureIdAndActiveIsTrueAndDeletedAtIsNull(String secureId);
}
