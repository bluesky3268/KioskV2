package project.kiosk.kiosk.service;

import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface MemberService {

    Member join(MemberJoinDTO memberJoinDTO);

    Member login(MemberLoginDTO memberLoginDTO);

    boolean idDuplicateCheck(String memberId);

    Member findMemberById(String memberId);

    void logout(HttpServletRequest request);

    Member updateMember(MemberUpdateDTO memberUpdateDTO);

}
