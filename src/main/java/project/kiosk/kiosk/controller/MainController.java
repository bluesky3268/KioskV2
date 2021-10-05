package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.kiosk.kiosk.dto.CartDTO;
import project.kiosk.kiosk.dto.OrderDTO;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
import project.kiosk.kiosk.dto.responseDto.MemberListResponseDto;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Order;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.repository.OrderRepository;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderRepository orderRepository;

    @GetMapping("/")
    public String mainPage(Model model, Pageable pageable, HttpSession session) {
        List<Member> members = memberService.findMemberByRole(Role.MANAGER);
        Page<Member> paging = memberService.memberPaging(pageable);

        model.addAttribute("members", members);
        model.addAttribute("paging", paging);
        return "index";
    }

    @GetMapping("/member/{no}")
    public String itemPage(@PathVariable Long no, Pageable pageable, Model model) {
        Page<Member> members = memberService.memberPaging(pageable);
        List<Item> items = itemService.findItems(no);

        for (Item item : items) {
            log.info("item : {}", item.getItemName());
        }
        model.addAttribute("members", members);
        model.addAttribute("items", items);
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

    @GetMapping("/order")
    public String order(HttpSession session) {
        ArrayList<CartDTO> cart = (ArrayList<CartDTO>) session.getAttribute("cart");

        int totalPrice = 0;
        for (int i = 0; i < cart.size(); i++) {
            int price = cart.get(i).getItem().getPrice();
            int quantity = cart.get(i).getQuantity();
            totalPrice += price * quantity;
        }

        session.setAttribute("totalPrice", totalPrice);
        return "main/payment";
    }

    @GetMapping("/payment")
    public String pay(HttpSession session) {

        List<CartDTO> cart = (ArrayList<CartDTO>) session.getAttribute("cart");
        for (CartDTO cartDTO : cart) {
            log.info("item in cart : {}, {}, {}, {}",cartDTO.getItem().getMemberId(), cartDTO.getItem().getItemName(), cartDTO.getQuantity(), cartDTO.getItem().getPrice());
        }

        for (CartDTO cartDTO : cart) {
            Member member = memberService.findMemberById(cartDTO.getItem().getMemberId());
            int price = cartDTO.getItem().getPrice();
            Item item = itemService.findItemEntity(cartDTO.getItem().getNo());

            Order order = new Order(item, price, cartDTO.getQuantity(), member);

            orderRepository.save(order);
        }

        return "main/success";
    }

    @GetMapping("/confirm")
    public String end(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
