package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.kiosk.kiosk.dto.CartDTO;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;
import project.kiosk.kiosk.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderService orderService;

    @GetMapping("/")
    public String mainPage(Model model, Pageable pageable, HttpSession session) {
        List<Member> members = memberService.findMemberByRole(Role.MANAGER);
        Page<Member> paging = memberService.memberPaging(pageable);

        model.addAttribute("members", members);
        model.addAttribute("paging", paging);
        return "index";
    }

    @GetMapping("/member/{no}")
    public String itemPage(@PathVariable Long no, @PageableDefault(size = 9, sort = "no", direction = Sort.Direction.ASC) Pageable pageable, Model model) {

        List<Member> members = memberService.findMemberByRole(Role.MANAGER);
        Page<Item> items = itemService.findByMemberNoWithPage(no, pageable);

        for (Item item : items) {
            log.info("item name : {}", item.getItemName());
        }

        int startPage = Math.max(1, items.getPageable().getPageNumber() - 4);
        int endPage = Math.min(items.getTotalPages(), items.getPageable().getPageNumber() + 4);

        model.addAttribute("memberNo", no);
        model.addAttribute("members", members);
        model.addAttribute("items", items);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "main/items";
    }

    @GetMapping("/member/{no}/item/{itemNo}")
    public String addItem(@PathVariable Long no, @PathVariable Long itemNo, @RequestParam("q") int quantity, HttpSession session, RedirectAttributes redirectAttributes) {

        log.info("add Item 호출");

        ItemResponseDto findItem = itemService.findItem(itemNo);

        CartDTO cart = new CartDTO();
        cart.setItem(findItem);
        cart.setQuantity(quantity);

        List<CartDTO> itemList = null;
        if (session.getAttribute("cart") == null) {
            itemList = new ArrayList<>();
        } else {
            itemList = (List<CartDTO>) session.getAttribute("cart");
        }
        itemList.add(cart);

        int totalPrice = 0;
        for (int i = 0; i < itemList.size(); i++) {
            int price = itemList.get(i).getItem().getPrice();
            int q = itemList.get(i).getQuantity();
            totalPrice += price * q;
        }

        session.setAttribute("totalPrice", totalPrice);
        session.setAttribute("cart", itemList);

        log.info("cart : {}, {}원, {}개", cart.getItem().getItemName(), cart.getItem().getPrice(), cart.getQuantity());

        redirectAttributes.addAttribute("no", no);
        return "redirect:/member/{no}";

    }

    @GetMapping("/order/reset/{memberNo}")
    public String cartReset(@PathVariable("memberNo") Long no, HttpSession session) {
        if (session.getAttribute("cart") != null) {
            session.removeAttribute("cart");
            session.removeAttribute("totalPrice");
        }
        log.info("memberNo : {}", no);

        return "redirect:/member/" + no;
    }

    @GetMapping("/order")
    public String order(HttpServletRequest request, HttpSession session) {
        try {
            ArrayList<CartDTO> cart = (ArrayList<CartDTO>) session.getAttribute("cart");

            int totalPrice = 0;
            for (int i = 0; i < cart.size(); i++) {
                int price = cart.get(i).getItem().getPrice();
                int quantity = cart.get(i).getQuantity();
                totalPrice += price * quantity;
            }

            session.setAttribute("totalPrice", totalPrice);
            return "main/payment";

        } catch (NullPointerException e) {
            String referer = request.getHeader("REFERER");
            log.info("장바구니에 상품이 없습니다.");
            return "redirect:" + referer;
        }
    }

    @GetMapping("/payment")
    public String pay(HttpSession session) {

        List<CartDTO> cart = (ArrayList<CartDTO>) session.getAttribute("cart");
        for (CartDTO cartDTO : cart) {
            log.info("item in cart : {}, {}, {}, {}",cartDTO.getItem().getMemberId(), cartDTO.getItem().getItemName(), cartDTO.getQuantity(), cartDTO.getItem().getPrice());
        }

        orderService.addOrder(cart);

        return "main/success";
    }

    @GetMapping("/confirm")
    public String end(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
