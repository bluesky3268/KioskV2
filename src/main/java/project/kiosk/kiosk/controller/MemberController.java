package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.service.MemberService;

import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String adminMain() {
        return "admin/adminMain";
    }

    @GetMapping("/member")
    public String addForm() {
        return "admin/join";
    }

    @PostMapping("/member")
    public String addMember(@ModelAttribute @Validated MemberJoinDTO memberJoin, BindingResult bindingResult) {
        log.info("memberJoin : {}, {}, {}, {}, {}", memberJoin.getId(), memberJoin.getPassword(), memberJoin.getLocation(), memberJoin.getRole(), memberJoin.getThumbImg());
//        if (!bindingResult.hasErrors()) {
            memberService.join(memberJoin);
            return "redirect:/admin";
//        }else{
//            log.info("bindingResult : {}",bindingResult.getAllErrors());
//        }
//        return "admin/join";
    }


    @ResponseBody
    @PostMapping("/duplicateCheck")
    public String idDuplicateCheck(@RequestBody HashMap<String, Object> shop) {
        String checkName = String.valueOf(shop.get("shop"));
        boolean duplicateCheck = memberService.idDuplicateCheck(checkName);
        if (duplicateCheck) {
            // 아이디 사용가능
            log.info("아이디 사용가능");
            return "0";
        } else {
            // 중복된 아이디 있음
            log.info("아이디 중복");
            return "1";
        }
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
    public String editMember(@PathVariable("no") Long memberNo, @RequestBody @Validated MemberUpdateDTO updateMember, BindingResult bindingResult, Model model) {
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
