package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
import project.kiosk.kiosk.dto.responseDto.MemberListResponseDto;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final ItemService itemService;
    private final MemberService memberService;

    //==== Member ====

    @GetMapping()
    public String adminMain(Model model, HttpSession session) {
        String id = String.valueOf(session.getAttribute("loggedIn"));
        Member findMember = memberService.findMemberById(id);
        model.addAttribute("memberNo", findMember.getNo());
        return "admin/adminMain";
    }

    @GetMapping("/member")
    public String addForm() {
        return "/admin/joinForm";
    }


    @GetMapping("/member/{no}")
    public String editMemberForm(@PathVariable("no") Long memberNo, Model model) {
        Member findMember = memberService.findMemberByMemberNo(memberNo);
        model.addAttribute("member", findMember);
        model.addAttribute("file", findMember.getThumbImg());
        return "admin/memberEditForm";
    }

    @GetMapping("/members")
    public String memberList(Model model) {
        List members = new ArrayList<>();
        try {
            members = memberService.findMemberByRole(Role.MANAGER);
        } catch (NullPointerException e) {
            log.info("데이터 없음 : {}", e.getStackTrace());
        }
        model.addAttribute("members", members);
        return "admin/memberList";
    }

    @GetMapping("/members/{role}")
    public String getMemberList(@PathVariable String role, Model model) {
        List members = new ArrayList<>();
        if (role.equals("supervisor")) {
            members = memberService.findMemberByRole(Role.SUPERVISOR);
            model.addAttribute("members", members);
            return "admin/memberListSupervisor";
        }else{
            members = memberService.findMemberByRole(Role.MANAGER);
            model.addAttribute("members", members);
            return "admin/memberListManager";
        }
    }

    //==== item ====

    @GetMapping("/item")
    public String itemAddForm(Model model) {
        List<Member> members = memberService.findMemberByRole(Role.MANAGER);
        model.addAttribute("members", members);
        return "admin/itemAddForm";
    }

    @GetMapping("/item/{itemNo}")
    public String itemEdit(@PathVariable Long itemNo, Model model) {
        ItemResponseDto item = itemService.findItem(itemNo);
        Member member = memberService.findMemberById(item.getMemberId());
        model.addAttribute("item", item);
        model.addAttribute("file", item.getThumbImg());
        model.addAttribute("memberNo", member.getNo());
        return "admin/itemEditForm";
    }

    @GetMapping("/items/{memberNo}")
    public String itemList(@PathVariable("memberNo") Long no, Model model, @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable, HttpSession session) {

        Page<Item> itemList = itemService.findByMemberNoWithPage(no, pageable);
        Member findMember = memberService.findMemberByMemberNo(no);

        List<Member> members = memberService.findMemberByRole(Role.MANAGER);

        model.addAttribute("memberId", findMember.getId());
        model.addAttribute("items", itemList);
        model.addAttribute("members", members);

        return "admin/itemListByMember";
    }

    @GetMapping("/items")
    public String itemListAll(Model model, @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable, HttpSession session) {
        Page<Item> itemList = itemService.findAll(pageable);
        List<Member> members = memberService.findMemberByRole(Role.MANAGER);

        model.addAttribute("items", itemList);
        model.addAttribute("members", members);

        return "admin/itemList";
    }


}
