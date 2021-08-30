package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.service.MemberService;

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
    public String login(MemberLoginDTO memberLogin) {
        memberService.login(memberLogin);
        return "admin/adminMain";
    }
}
