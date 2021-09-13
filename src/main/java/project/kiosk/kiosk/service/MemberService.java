package project.kiosk.kiosk.service;

import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface MemberService {

    Member joinInit(MemberJoinDTO memberJoinDTO);

    Member join(MemberJoinDTO memberJoinDTO);

    Member login(MemberLoginDTO memberLoginDTO);

    boolean idDuplicateCheck(String loginId);

    Member findMemberByLoginId(String loginId);

    Member findMemberByMemberNo(Long memberNo);

    List<Member> findMemberByRole(Role role);

    String logout(HttpServletRequest request);

    Member updateMember(Member member, MemberUpdateDTO updateMember);

    void deleteMember(Long memberNo);

}
