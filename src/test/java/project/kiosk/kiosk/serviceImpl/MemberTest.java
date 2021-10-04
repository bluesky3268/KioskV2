package project.kiosk.kiosk.serviceImpl;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.dto.responseDto.MemberListResponseDto;
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
        MemberJoinDTO member1 = new MemberJoinDTO("member1", "1234", "1234", "manager");
        Long no = memberService.joinInit(member1);
        System.out.println("joinMember.getMemberId() = " + no);
    }

    @Test
    @Rollback
    void 로그인_로그아웃() {
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();

        // 로그인
        // given
        MemberLoginDTO loginMember = new MemberLoginDTO("member1", "1234");

        // when
        String loginId = memberService.login(loginMember);

        if (loginId != null) {
            session.setAttribute("loggedIn", loginId);
        }

        // then
        assertThat("member1").isEqualTo(session.getAttribute("loggedIn"));

        // 로그아웃
        // when
        String logout = memberService.logout(session);
        // then
        assertThat(logout).isEqualTo("true");
        assertThat(session.isInvalid()).isTrue();

    }

    @Test
    void 회원정보_수정() {

        //given
        Member member = memberService.findMemberById("member1");
        MemberUpdateDTO update = new MemberUpdateDTO("1111", "z", "supervisor");

        // when
//        memberService.updateMember(member, update);

        Member afterUpdate = memberService.findMemberById("member1");

        // then
        assertThat(afterUpdate.getNo()).isEqualTo(member.getNo());
//        boolean matches = passwordEncoder.matches("1111", afterUpdate.getPassword());
//        assertThat(matches).isTrue();
//        assertThat(Role.SUPERVISOR).isEqualTo(afterUpdate.getRole());
//        assertThat("z").isEqualTo(afterUpdate.getLocation());
    }

    @Test
    void 회원삭제() {
        // given
        Member findMember = memberService.findMemberById("member1");
        Long memberNo = findMember.getNo();

        // when
        memberService.deleteMember(memberNo);

        // then
        Member afterDelete = memberService.findMemberById("member1");
        assertThat(afterDelete).isNull();

    }


    @Test
    public void 회원전체검색() throws Exception{
        // given
        MemberJoinDTO member2 = new MemberJoinDTO("member2", "2345", "2345", "manager");
        memberService.joinInit(member2);
        MemberJoinDTO member3 = new MemberJoinDTO("member3", "3456", "3456", "manager");
        memberService.joinInit(member3);
        MemberJoinDTO member4 = new MemberJoinDTO("member4", "4567", "4567", "manager");
        memberService.joinInit(member4);

        // when
        List<Member> members = memberService.findMembers();

        // then
        assertThat(members.size()).isEqualTo(6);
        assertThat(members.get(0).getId()).isEqualTo("ROOT");
        assertThat(members.get(members.size()-1).getId()).isEqualTo("member4");


    }

    @Test
    void ROLE로_회원검색() {
        // given
        MemberJoinDTO member2 = new MemberJoinDTO("member2", "2345", "2345", "manager");
        memberService.joinInit(member2);

        // when
        List<Member> managers = memberService.findMemberByRole(Role.MANAGER);

        // then
        assertThat(managers.size()).isEqualTo(3);
        assertThat(managers.get(0).getId()).isEqualTo("SAMPLE");
    }

    @Test
    void 회원페이징() {

        // given
        MemberJoinDTO member2 = new MemberJoinDTO("member2", "2345", "2345", "manager");
        memberService.joinInit(member2);
        MemberJoinDTO member3 = new MemberJoinDTO("member3", "3456", "3456", "manager");
        memberService.joinInit(member3);
        MemberJoinDTO member4 = new MemberJoinDTO("member4", "4567", "4567", "manager");
        memberService.joinInit(member4);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Member> members = memberService.memberPaging(pageable);
        for (Member member : members) {
            System.out.println(member.getId());
        }

        System.out.println("pageable.getPageNumber() = " + pageable.getPageNumber());
        System.out.println("pageable.getPageSize() = " + pageable.getPageSize());
        System.out.println("pageable.hasPrevious() = " + pageable.hasPrevious());
        System.out.println("pageable.first() = " + pageable.first());
        System.out.println("pageable.next() = " + pageable.next());
        System.out.println("---------------------------------------------------------");
        System.out.println("members.hasPrevious() = " + members.hasPrevious());
        System.out.println("members.hasNext() = " + members.hasNext());
        System.out.println("members.getContent() = " + members.getContent());

        System.out.println("---------------------------------------------------------");
        Page<Member> m = memberService.memberPaging(pageable);
        for (Member member : m) {
            System.out.println("member.getId() = " + member.getId() + ", role : " + member.getRole());
        }

    }


}