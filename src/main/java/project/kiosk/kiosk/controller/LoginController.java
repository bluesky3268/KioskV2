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
    public String login() {
        return "admin/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated MemberLoginDTO memberLogin, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "admin/loginForm";
        }

        // 로그인 성공
        Member loginMember = memberService.login(memberLogin);
        if (loginMember != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedIn", loginMember.getMemberId());
            return "redirect:" + redirectURL;
        }

        // 로그인 실패
        bindingResult.reject("loginFail", "올바르지 않은 아이디 혹은 비밀번호입니다.");
        return "admin/adminMain";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        memberService.logout(request);
        return "redirect:/";
    }
}
