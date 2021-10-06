package project.kiosk.kiosk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.kiosk.kiosk.entity.Order;

public interface OrderService {

    Page<Order> findOrdersByMemberNoWithPaging(Long memberNo, Pageable pageable);

}
