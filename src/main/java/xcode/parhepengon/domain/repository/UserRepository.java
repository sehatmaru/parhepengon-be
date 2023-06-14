package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.UserModel;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

    @Query(value = "SELECT * FROM t_user" +
            " WHERE username = :username AND active IS TRUE" +
            " AND deleted_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    Optional<UserModel> getActiveUserByUsername(String username);

    @Query(value = "SELECT * FROM t_user" +
            " WHERE secure_id = :secureId AND deleted_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    Optional<UserModel> getUserBySecureId(String secureId);

    @Query(value = "SELECT * FROM t_user" +
            " WHERE secure_id = :secureId AND active IS TRUE" +
            " AND deleted_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    Optional<UserModel> getActiveUserBySecureId(String secureId);
}
