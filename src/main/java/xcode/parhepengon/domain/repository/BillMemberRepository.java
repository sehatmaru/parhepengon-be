package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.BillMemberModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillMemberRepository extends JpaRepository<BillMemberModel, String> {

    Optional<BillMemberModel> findByBillAndMember(String bill, String member);
    List<BillMemberModel> findAllByBill(String bill);
}
