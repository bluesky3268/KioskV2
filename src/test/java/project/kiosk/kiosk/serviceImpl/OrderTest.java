package project.kiosk.kiosk.serviceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.entity.Order;
import project.kiosk.kiosk.service.OrderService;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class OrderTest {

    @Autowired
    private OrderService orderService;

    @Test
    void 주문목록_조회() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<Order> result = orderService.findOrdersByMemberNoWithPaging(3L, pageable);

        for (Order order : result) {
            System.out.println("order = " + order.toString());
        }

    }

}