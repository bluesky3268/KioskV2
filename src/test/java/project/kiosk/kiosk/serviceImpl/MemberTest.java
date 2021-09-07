package project.kiosk.kiosk.serviceImpl;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
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

    @BeforeEach
    void 회원등록() {
        MemberJoinDTO member1 = new MemberJoinDTO("member2", "1234", "1234", Role.MANAGER);
        Member joinMember = memberService.joinInit(member1);
        System.out.println("joinMember.getMemberId() = " + joinMember.getLoginId());
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
            session.setAttribute("loggedIn", login.getLoginId());
        }

        assertThat("member1").isEqualTo(session.getAttribute("loggedIn"));

        // 로그아웃
        memberService.logout(request);
        assertThat(session).isNull();

    }

}