package xcode.parhepengon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.parhepengon.domain.model.GroupMemberModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMemberModel, String> {
    Optional<GroupMemberModel> findByGroupAndMemberAndLeaveAtIsNull(String group, String member);
    Optional<List<GroupMemberModel>> findAllByGroupAndLeaveAtIsNull(String secureId);
}
