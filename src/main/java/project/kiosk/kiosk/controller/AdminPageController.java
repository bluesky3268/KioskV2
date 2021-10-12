package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Order;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;
import project.kiosk.kiosk.service.OrderService;

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
    private final OrderService orderService;

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
        return "admin/member/joinForm";
    }


    @GetMapping("/member/{no}")
    public String editMemberForm(@PathVariable("no") Long memberNo, Model model) {
        Member findMember = memberService.findMemberByMemberNo(memberNo);
        model.addAttribute("member", findMember);
        model.addAttribute("file", findMember.getThumbImg());
        return "admin/member/memberEditForm";
    }

    @GetMapping("/members")
    public String memberList(Model model, @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> members = null;

        try {
            members = memberService.findMemberByRoleWithPage(Role.MANAGER, pageable);
            int startPage = Math.max(1, members.getPageable().getPageNumber() - 4);
            int endPage = Math.min(members.getTotalPages(), members.getPageable().getPageNumber() + 4);

            model.addAttribute("members", members);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        } catch (NullPointerException e) {
            log.info("데이터 없음 : {}", e.getStackTrace());
            return "admin/member/joinForm";
        }

        return "admin/member/memberList";
    }

    @GetMapping("/members/{role}")
    public String getMemberList(@PathVariable String role, Model model, @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable) {

        if (role.equals("supervisor")) {
            Page<Member> members = memberService.findMemberByRoleWithPage(Role.SUPERVISOR, pageable);

            int startPage = Math.max(1, members.getPageable().getPageNumber() - 4);
            int endPage = Math.min(members.getTotalPages(), members.getPageable().getPageNumber() + 4);

            model.addAttribute("members", members);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "admin/member/memberListSupervisor";
        }else{
            Page<Member> members = memberService.findMemberByRoleWithPage(Role.MANAGER, pageable);

            int startPage = Math.max(1, members.getPageable().getPageNumber() - 4);
            int endPage = Math.min(members.getTotalPages(), members.getPageable().getPageNumber() + 4);

            model.addAttribute("members", members);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "admin/member/memberListManager";
        }

    }

    //==== item ====

    @GetMapping("/item")
    public String itemAddForm(Model model, HttpSession session) {

        String role = String.valueOf(session.getAttribute("role"));

        if (role.equals("MANAGER")) {
            String memberId = String.valueOf(session.getAttribute("loggedIn"));
            Member findMember = memberService.findMemberById(memberId);
            model.addAttribute("member", findMember);
        }else{
            List<Member> members = memberService.findMemberByRole(Role.MANAGER);
            model.addAttribute("members", members);

            String memberId = String.valueOf(session.getAttribute("loggedIn"));
            Member findMember = memberService.findMemberById(memberId);
            model.addAttribute("member", findMember);

        }

        return "admin/item/itemAddForm";
    }

    @GetMapping("/item/{itemNo}")
    public String itemEdit(@PathVariable Long itemNo, Model model) {
        ItemResponseDto item = itemService.findItem(itemNo);
        Member member = memberService.findMemberById(item.getMemberId());

        model.addAttribute("item", item);
        model.addAttribute("file", item.getThumbImg());
        model.addAttribute("memberNo", member.getNo());
        return "admin/item/itemEditForm";
    }

    @GetMapping("/items/{memberNo}")
    public String itemList(@PathVariable("memberNo") Long no, Model model, @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable, HttpSession session) {

        String role = String.valueOf(session.getAttribute("role"));
        Page<Item> itemList = itemService.findByMemberNoWithPage(no, pageable);
        Member findMember = memberService.findMemberByMemberNo(no);

        int startPage = Math.max(1, itemList.getPageable().getPageNumber() - 4);
        int endPage = Math.min(itemList.getTotalPages(), itemList.getPageable().getPageNumber() + 4);

        if (role.equals("SUPERVISOR")) {
            List<Member> members = memberService.findMemberByRole(Role.MANAGER);

            model.addAttribute("member", findMember);
            model.addAttribute("members", members);
            model.addAttribute("items", itemList);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "admin/item/itemListByMember";

        }else{

            model.addAttribute("member", findMember);
            model.addAttribute("items", itemList);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "/admin/item/itemListForMember";
        }
    }

    @GetMapping("/items")
    public String itemListAll(Model model, @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Item> itemList = itemService.findAll(pageable);
        List<Member> members = memberService.findMemberByRole(Role.MANAGER);

        int startPage = Math.max(1, itemList.getPageable().getPageNumber() - 4);
        int endPage = Math.min(itemList.getTotalPages(), itemList.getPageable().getPageNumber() + 4);

        model.addAttribute("items", itemList);
        model.addAttribute("members", members);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/item/itemList";
    }

    //==== order ====

    @GetMapping("/orders")
    public String orderListAll(@PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable, Model model, HttpSession session) {
        String role = String.valueOf(session.getAttribute("role"));
        if (role.equals("SUPERVISOR")) {
            List<Member> members = memberService.findMemberByRole(Role.MANAGER);
            Page<Order> orders = orderService.findAll(pageable);

            log.info("orders size : {}", orders.getSize());
            for (Order order : orders) {
                log.info("order regDate : {}", order.getRegDate());
            }

            int startPage = Math.max(1, orders.getPageable().getPageNumber() - 4);
            int endPage = Math.min(orders.getTotalPages(), orders.getPageable().getPageNumber() + 4);
            log.info("orders startPage : {}", startPage);
            log.info("orders endPage : {}", endPage);

            model.addAttribute("orders", orders);
            model.addAttribute("members", members);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);


            return "admin/order/orderList";
        }else{
            String id = String.valueOf(session.getAttribute("loggedIn"));
            Member member = memberService.findMemberById(id);

            Page<Order> orders = orderService.findOrdersByMemberNoWithPaging(member.getNo(), pageable);
            for (Order order : orders) {
                log.info("order regDate : {}", order.getRegDate());
            }


            int startPage = Math.max(1, orders.getPageable().getPageNumber() - 4);
            int endPage = Math.min(orders.getTotalPages(), orders.getPageable().getPageNumber() + 4);

            model.addAttribute("member", member);
            model.addAttribute("orders", orders);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "admin/order/orderListForMember";
        }

    }

    @GetMapping("/orders/member/{memberNo}")
    public String orderListByMember(@PathVariable Long memberNo, @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable, Model model) {

        Page<Order> orders = orderService.findOrdersByMemberNoWithPaging(memberNo, pageable);
        List<Member> members = memberService.findMemberByRole(Role.MANAGER);

        for (Order order : orders) {
            log.info("regDate : {}", order.getRegDate());
        }

        int startPage = Math.max(1, orders.getPageable().getPageNumber() - 4);
        int endPage = Math.min(orders.getTotalPages(), orders.getPageable().getPageNumber() + 4);

        model.addAttribute("members", members);
        model.addAttribute("orders", orders);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/order/orderListByMember";
    }

}
