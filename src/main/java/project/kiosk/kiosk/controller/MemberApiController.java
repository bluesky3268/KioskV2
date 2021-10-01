package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.entity.UploadFile;
import project.kiosk.kiosk.service.MemberService;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final MemberService memberService;

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



    @GetMapping("/member/{no}")
    public String editMemberForm(@PathVariable("no") Long memberNo, Model model) {
        Member findMember = memberService.findMemberByMemberNo(memberNo);
        model.addAttribute("member", findMember);
        return "admin/memberEditForm";
    }

    @PatchMapping("/member/{no}")
    public String editMember(@PathVariable("no") Long memberNo, @RequestBody @Validated MemberUpdateDTO updateMember, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/memberEditForm";
        }

        Member findMember = memberService.findMemberByMemberNo(memberNo);
        Member updatedMember = memberService.updateMember(findMember, updateMember);

        model.addAttribute("update", updatedMember);
        return "redirect:/admin";
    }

    @DeleteMapping("/member/{no}")
    public String deleteMember(@PathVariable("no") Long memberNo) {
        memberService.deleteMember(memberNo);
        return "redirect:/members";
    }

    @ResponseBody
    @GetMapping("/memberImages/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        Resource urlResource = memberService.downloadImage(filename);
        log.info("urlResource : {}", urlResource);

        return urlResource;
    }
}
