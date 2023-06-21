package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.BillMemberModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillMemberRepository extends JpaRepository<BillMemberModel, String> {

    @Query(value = "SELECT * FROM t_bill_member" +
            " WHERE bill_secure_id = :bill AND member_secure_id = :member" +
            " AND deleted IS FALSE" +
            " LIMIT 1", nativeQuery = true)
    Optional<BillMemberModel> getBillMember(String bill, String member);

    @Query(value = "SELECT * FROM t_bill_member" +
            " WHERE member_secure_id = :user" +
            " AND deleted IS FALSE", nativeQuery = true)
    List<BillMemberModel> getBillByUser(String user);

    @Query(value = "SELECT * FROM t_bill_member" +
            " WHERE bill_secure_id = :bill AND deleted IS FALSE", nativeQuery = true)
    List<BillMemberModel> getBillMemberList(String bill);
}
