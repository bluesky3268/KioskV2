package project.kiosk.kiosk.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.util.List;

public interface MemberService {

    Long joinInit(MemberJoinDTO memberJoinDTO);

    Long join(MemberJoinDTO memberJoinDTO);

    String login(MemberLoginDTO memberLoginDTO);

    boolean idDuplicateCheck(String loginId);

    Member findMemberById(String loginId);

    Member findMemberByMemberNo(Long memberNo);

    List<Member> findMembers();

    Page<Member> memberPaging(Pageable pageable);

    List<Member> findMemberByRole(Role role);

    String logout(HttpSession session);

    Member updateMember(Long memberNo, MemberUpdateDTO updateMember, MultipartFile multipartFile);

    void deleteMember(Long memberNo);

    Resource downloadImage(String fileName) throws MalformedURLException;

}
