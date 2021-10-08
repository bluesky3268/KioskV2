package project.kiosk.kiosk.controller.apiController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.kiosk.kiosk.dto.CartDTO;
import project.kiosk.kiosk.dto.OrderDTO;
import project.kiosk.kiosk.entity.Order;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class orderApiController {

    private final OrderService orderService;

    @GetMapping("/orders/member/{memberNo}")
    public Page<Order> orderListByMember(@PathVariable Long memberNo, Pageable pageable, Model model) {

        Page<Order> orders = orderService.findOrdersByMemberNoWithPaging(memberNo, pageable);

//        model.addAttribute("orders", orders);
        return orders;
    }


}
