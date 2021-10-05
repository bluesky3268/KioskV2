package project.kiosk.kiosk.controller.apiController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kiosk.kiosk.dto.CartDTO;
import project.kiosk.kiosk.dto.OrderDTO;
import project.kiosk.kiosk.entity.Order;
import project.kiosk.kiosk.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class mainApiController {

    private final ItemService itemService;


    @GetMapping("/payment")
    public List<Order> addOrder(@RequestBody OrderDTO orderDTO) {

        log.info("orderDTO : {}", orderDTO);

        return null;
    }


}
