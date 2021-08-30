package project.kiosk.kiosk.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.repository.MemberRepository;
import project.kiosk.kiosk.service.MemberService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoin) {
        if (memberJoin == null) {
            return;
        }

        if (idCheck(memberJoin.getMemberId()) && pwdCheck(memberJoin.getPassword(), memberJoin.getPasswordConfirm())) {

            LocalDateTime regDate = LocalDateTime.now();
            String encodedPwd = passwordEncoder.encode(memberJoin.getPassword());

            Member member = new Member(memberJoin.getMemberId(),
                    encodedPwd,
                    memberJoin.getLocation(),
                    regDate,
                    memberJoin.getRole(),
                    memberJoin.getThumbImg());
        }
    }

    private boolean pwdCheck(String password, String passwordConfirm) {
        return true;
    }

    private boolean idCheck(String memberId) {
        return true;
    }

    @Override
    public Member login(MemberLoginDTO memberLoginDTO) {
        return null;
    }

    @Override
    public void logout() {

    }

    @Override
    public Member update(MemberUpdateDTO memberUpdateDTO) {
        return null;
    }
}
