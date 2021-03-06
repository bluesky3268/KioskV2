package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.responseDto.MemberListResponseDto;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm() {
        return "admin/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated MemberLoginDTO memberLogin, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {

        log.info("login : {}, {}", memberLogin.getLoginId(), memberLogin.getLoginPwd());
        if (bindingResult.hasErrors()) {
            log.info("로그인 바인딩 에러");
            return "admin/loginForm";
        }

        // 로그인 성공
        String loginMemberId = memberService.login(memberLogin);
        Member findMember = memberService.findMemberById(loginMemberId);
        if (loginMemberId != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedIn", loginMemberId);
            session.setAttribute("role", findMember.getRole());
            log.info("로그인 성공");
            return "redirect:/admin";
        }

        // 로그인 실패
        bindingResult.reject("loginFail", "올바르지 않은 아이디 혹은 비밀번호입니다.");
        return "admin/loginForm";
    }

    @GetMapping("/log_out")
    public String logout(HttpSession session) {
        String logout = memberService.logout(session);
        if (logout.equals("true")) {
            log.info("로그아웃 성공");
            return "redirect:/login";
        }
        return "redirect:/";
    }

}
