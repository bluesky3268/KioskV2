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
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/members")
    public String memberList(Model model) {
        List<Member> members = memberService.findMemberByRole(Role.MANAGER);
        model.addAttribute("members", members);
        return "admin/memberList";
    }

    @GetMapping("/member")
    public String addForm() {
        return "/admin/joinForm";
    }


    /**
     * 매장 등록 : API로 바꿔야 함
     */
    @PostMapping("/member")
    public String addMember(@ModelAttribute @Validated MemberJoinDTO memberJoin, BindingResult bindingResult) {
//        log.info("memberJoin : {}, {}, {}, {}, {}", memberJoin.getId(), memberJoin.getPassword(), memberJoin.getLocation(), memberJoin.getRole(), memberJoin.getThumbImg());
        if (!bindingResult.hasErrors()) {
            memberService.join(memberJoin);
            return "redirect:/admin";
        }else{
            log.info("bindingResult : {}",bindingResult.getAllErrors());
        }
        return "redirect:/admin/members";
    }

    @GetMapping("/member/{no}")
    public String editMemberForm(@PathVariable("no") Long memberNo, Model model) {
        Member findMember = memberService.findMemberByMemberNo(memberNo);
        model.addAttribute("member", findMember);
        model.addAttribute("file", findMember.getThumbImg());
        return "admin/memberEditForm";
    }

    //==== item ====

    @GetMapping("/item")
    public String itemAddForm(Model model) {
        List<Member> members = memberService.findMemberByRole(Role.MANAGER);
        model.addAttribute("members", members);
        return "admin/itemAddForm";
    }

    /**
     * 상품 등록 : API로 바꿔야 함
     */
    @PostMapping("/item")
    public String itemAdd(@ModelAttribute @Validated ItemAddDTO itemAdd, BindingResult bindingResult, HttpSession session) {
        Long itemNo = 0L;

        if (!bindingResult.hasErrors()) {
            String memberId = String.valueOf(session.getAttribute("loggedIn"));
            Member findMember = memberService.findMemberById(memberId);
            itemAdd.setMemberNo(findMember.getNo());
            itemService.addItem(itemAdd, findMember.getNo());
        }else{
            log.info("bindingError : {}", bindingResult.getAllErrors());
        }

        return "redirect:/admin";
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
        model.addAttribute("items", itemList);

        return "admin/itemList";
    }

    @GetMapping("/items")
    public String itemListAll(Model model, @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable, HttpSession session) {
        Page<Item> itemList = itemService.findAll(pageable);

        model.addAttribute("items", itemList);

        return "admin/itemList";
    }


}
