package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class JoinController {

    private final MemberService memberService;

    @GetMapping
    public String join() {
        return "admin/joinForm";
    }

    @PostMapping()
    public String join(@Validated MemberJoinDTO memberJoin, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/joinForm";
        }
        memberService.join(memberJoin);
        return "admin/adminMain";
    }
}
