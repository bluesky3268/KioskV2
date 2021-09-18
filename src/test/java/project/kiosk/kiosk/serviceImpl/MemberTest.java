package project.kiosk.kiosk.serviceImpl;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.repository.MemberRepository;
import project.kiosk.kiosk.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void 회원등록() {
        MemberJoinDTO member1 = new MemberJoinDTO("member1", "1234", "1234", Role.MANAGER);
        Member joinMember = memberService.joinInit(member1);
        System.out.println("joinMember.getMemberId() = " + joinMember.getId());
    }

    @Test
    @Rollback
    void 로그인_로그아웃() {
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();

        // 로그인
        MemberLoginDTO loginMember = new MemberLoginDTO("member1", "1234");
        Member login = memberService.login(loginMember);

        if (login != null) {
            session.setAttribute("loggedIn", login.getId());
        }

        assertThat("member1").isEqualTo(session.getAttribute("loggedIn"));

        // 로그아웃
        String logout = memberService.logout(session);
        assertThat(logout).isEqualTo("true");
        assertThat(session.isInvalid()).isTrue();

    }

    @Test
    void 회원정보_수정() {
        Member member = memberService.findMemberById("member1");
        MemberUpdateDTO update = new MemberUpdateDTO("1111", "z", Role.SUPERVISOR);

        memberService.updateMember(member, update);

        Member afterUpdate = memberService.findMemberById("member1");

        assertThat(afterUpdate.getNo()).isEqualTo(member.getNo());
        boolean matches = passwordEncoder.matches("1111", afterUpdate.getPassword());
        assertThat(matches).isTrue();
        assertThat(Role.SUPERVISOR).isEqualTo(afterUpdate.getRole());
        assertThat("z").isEqualTo(afterUpdate.getLocation());
    }

    @Test
    void 회원삭제() {
        Member findMember = memberService.findMemberById("member1");
        Long memberNo = findMember.getNo();
        memberService.deleteMember(memberNo);

        Member afterDelete = memberService.findMemberById("member1");
        assertThat(afterDelete).isNull();

    }


}