package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final ItemService itemService;

    public String adminMain() {
        return "admin/adminMain";
    }

    @GetMapping("/members")
    public String memberList() {
        return "admin/memberList";
    }

    @GetMapping("/members/{no}")
    public String editMemberForm(@PathVariable("no") Long memberNo, Model model) {
        Member findMember = memberService.findMemberByMemberNo(memberNo);
        model.addAttribute("member", findMember);
        return "admin/editForm";
    }

    @PatchMapping("/members/{no}")
    public String editMember(@PathVariable("no") Long memberNo, @Validated MemberUpdateDTO updateMember, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/editForm";
        }

        Member findMember = memberService.findMemberByMemberNo(memberNo);
        Member updatedMember = memberService.updateMember(findMember, updateMember);

        model.addAttribute("update", updatedMember);
        return "redirect:/admin";
    }

    @DeleteMapping("/members/{no}")
    public String deleteMember(@PathVariable("no") Long memberNo) {
        memberService.deleteMember(memberNo);
        return "redirect:/members";
    }
}
