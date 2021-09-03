package project.kiosk.kiosk.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.kiosk.kiosk.dto.FileStore;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.UploadFile;
import project.kiosk.kiosk.repository.MemberRepository;
import project.kiosk.kiosk.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStore fileStore;

    @Override
    public Member join(MemberJoinDTO memberJoin) {
        if (memberJoin == null) {
            return null;
        }

        if (idCheck(memberJoin.getMemberId()) && pwdCheck(memberJoin.getPassword(), memberJoin.getPasswordConfirm())) {

            LocalDateTime regDate = LocalDateTime.now();
            String encodedPwd = passwordEncoder.encode(memberJoin.getPassword());

            UploadFile uploadFile = null;

            try {
                uploadFile = fileStore.saveFile(memberJoin.getThumbImg());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Member newMember = new Member(memberJoin.getMemberId(),
                    encodedPwd,
                    memberJoin.getLocation(),
                    regDate,
                    memberJoin.getRole(),
                    uploadFile);

            return newMember;
        }
        return null;
    }

    @Override
    public boolean idDuplicateCheck(String memberId) {
        Member findMember = memberRepository.findByMemberId(memberId);
        if (findMember == null){
            // DB에 저장된 값이 없으면 사용 가능
            return true;
        }
//        DB에 저장된 값이 있으면 아이디 중복
        return false;

    }
    
    @Override
    public Member login(MemberLoginDTO memberLoginDTO) {
        Member findMember = memberRepository.findByMemberId(memberLoginDTO.getLoginId());

        if (findMember != null) {
            if (passwordEncoder.matches(findMember.getPassword(), memberLoginDTO.getLoginPwd())) {
                return findMember;
            }
        }
        log.info("올바르지 않은 아이디 혹은 비밀번호");
        return null;

    }

    @Override
    public Member findMemberById(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    private boolean pwdCheck(String password, String passwordConfirm) {
        return true;
    }

    private boolean idCheck(String memberId) {
        return true;
    }


    @Override
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

    }



    @Override
    public Member updateMember(MemberUpdateDTO memberUpdateDTO) {
        return null;
    }
}
