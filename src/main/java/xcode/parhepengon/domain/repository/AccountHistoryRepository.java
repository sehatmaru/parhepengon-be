package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.AccountHistoryModel;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistoryModel, String> {

}
