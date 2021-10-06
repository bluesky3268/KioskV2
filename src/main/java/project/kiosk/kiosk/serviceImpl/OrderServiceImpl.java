package project.kiosk.kiosk.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.entity.Order;
import project.kiosk.kiosk.repository.OrderRepository;
import project.kiosk.kiosk.service.OrderService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Page<Order> findOrdersByMemberNoWithPaging(Long memberNo, Pageable pageable) {
        return orderRepository.findAllByMemberNo(memberNo, pageable);
    }
}
