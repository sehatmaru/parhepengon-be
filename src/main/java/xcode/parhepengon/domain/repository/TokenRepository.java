package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.TokenModel;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenModel, String> {
    Optional<TokenModel> findByToken(String token);

    Optional<TokenModel> findByTokenAndTemporaryIsTrue(String token);
}
