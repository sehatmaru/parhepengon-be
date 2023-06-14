package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.SettingModel;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<SettingModel, String> {
    Optional<SettingModel> findByUser(String user);
}
