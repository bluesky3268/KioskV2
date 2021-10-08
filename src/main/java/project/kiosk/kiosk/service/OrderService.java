package project.kiosk.kiosk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.kiosk.kiosk.dto.CartDTO;
import project.kiosk.kiosk.entity.Order;

import java.util.List;

public interface OrderService {

    Page<Order> findOrdersByMemberNoWithPaging(Long memberNo, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    void addOrder(List<CartDTO> cart);
}
