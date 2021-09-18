package project.kiosk.kiosk.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.dto.FileStore;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.entity.UploadFile;
import project.kiosk.kiosk.repository.MemberRepository;
import project.kiosk.kiosk.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStore fileStore;
    private final FileServiceImpl fileService;

    @Override
    public Member joinInit(MemberJoinDTO memberJoinDTO) {
        LocalDateTime regDate = LocalDateTime.now();
        String encodedPwd = passwordEncoder.encode(memberJoinDTO.getPassword());

        Member member = null;
        Role role = null;
        if (memberJoinDTO.getRole().equals("supervisor")) {
            role = Role.SUPERVISOR;
        }else{
            role = Role.MANAGER;
        }
        member = new Member(memberJoinDTO.getId(), encodedPwd, regDate, role);

        log.info("joinMember : {}", memberJoinDTO.getId());

        Member savedMember = memberRepository.save(member);

        log.info("savedMember : {}", savedMember.getId());

        return savedMember;
    }

    @Override
    public Member join(MemberJoinDTO memberJoin) {
        if (memberJoin == null) {
            return null;
        }

        if (idCheck(memberJoin.getId()) && pwdCheck(memberJoin.getPassword(), memberJoin.getPasswordConfirm())) {

            LocalDateTime regDate = LocalDateTime.now();
            String encodedPwd = passwordEncoder.encode(memberJoin.getPassword());

            UploadFile uploadFile = null;
            try {
                uploadFile = fileStore.saveFile(memberJoin.getThumbImg());
                log.info("uploadFile 변환 성공 : {}, {}", uploadFile.getOriginalName(), uploadFile.getSaveName());
                fileService.addFile(uploadFile);
                log.info("uploadFile 저장 성공");

            } catch (IOException e) {
                e.printStackTrace();
            }

            Member member = null;
            Role role = null;

            if(memberJoin.getRole().equals("supervisor")){
                role = Role.SUPERVISOR;
            }else{
                role = Role.MANAGER;
            }

            member = new Member(memberJoin.getId(),
                    encodedPwd,
                    memberJoin.getLocation(),
                    regDate,
                    role,
                    uploadFile);


            Member savedMember = memberRepository.save(member);
            log.info("등록 성공");
            return savedMember;
        }
        return null;
    }

    @Override
    public boolean idDuplicateCheck(String id) {
        Member findMember = memberRepository.findMemberById(id);
        if (findMember == null){
            // DB에 저장된 값이 없으면 사용 가능
            return true;
        }
//        DB에 저장된 값이 있으면 아이디 중복
        return false;

    }
    
    @Override
    public Member login(MemberLoginDTO memberLoginDTO) {
        Member findMember = memberRepository.findMemberById(memberLoginDTO.getLoginId());

        if (findMember != null) {
            log.info(memberLoginDTO.getLoginPwd());
            log.info(findMember.getPassword());
            if (passwordEncoder.matches(memberLoginDTO.getLoginPwd(), findMember.getPassword())) {
                return findMember;
            }else{
                log.info("올바르지 않은 비밀번호");
            }
        }
        log.info("올바르지 않은 아이디");
        return null;

    }

    @Override
    public String logout(HttpSession session) {
//        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            return "true";
        }else{
            log.info("로그인이 먼저 필요");
            return "false";
        }
    }

    @Override
    public List<Member> findMemberByRole(Role role) {
        return memberRepository.findByRoleLike(role);
    }

    @Override
    public Member findMemberById(String id) {
        return memberRepository.findMemberById(id);
    }

    @Override
    public Member findMemberByMemberNo(Long no) {
        return memberRepository.findMemberByNo(no);
    }

    private boolean pwdCheck(String password, String passwordConfirm) {
        return true;
    }

    private boolean idCheck(String loginId) {
        return true;
    }

    @Override
    public Member updateMember(Member member, MemberUpdateDTO updateMember) {

        String encodedPwd = passwordEncoder.encode(updateMember.getPassword());
        if (updateMember.getThumbImg() != null) {
            try {
                UploadFile newImg = fileStore.saveFile(updateMember.getThumbImg());
                member.setThumbImg(newImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Role role = null;
        if (updateMember.getRole().equals("supervisor")) {
            role = Role.SUPERVISOR;
        } else {
            role = Role.MANAGER;
        }
        member.setPassword(encodedPwd);
        member.setLocation(updateMember.getLocation());
        member.setRole(role);

        return member;
    }

    @Override
    public void deleteMember(Long memberNo) {
        memberRepository.deleteMemberByNo(memberNo);
    }
}
