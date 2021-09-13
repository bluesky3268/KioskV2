package project.kiosk.kiosk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findMemberById(String id);

    Member findMemberByNo(Long no);

    List<Member> findByRoleLike(Role role);

    @Transactional
    void deleteMemberByNo(Long no);

}
