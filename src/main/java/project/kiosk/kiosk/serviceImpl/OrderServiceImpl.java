package project.kiosk.kiosk.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.dto.CartDTO;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Order;
import project.kiosk.kiosk.repository.OrderRepository;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;
import project.kiosk.kiosk.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    @Override
    public Page<Order> findOrdersByMemberNoWithPaging(Long memberNo, Pageable pageable) {
        pageable = Pageable.ofSize(5);
        return orderRepository.findAllByMemberNo(memberNo, pageable);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public void addOrder(List<CartDTO> cart) {
        for (CartDTO cartDTO : cart) {
            Member member = memberService.findMemberById(cartDTO.getItem().getMemberId());
            int price = cartDTO.getItem().getPrice();
            Item item = itemService.findItemEntity(cartDTO.getItem().getNo());

            int totalPrice = price * cartDTO.getQuantity();

            Order order = new Order(item, price, cartDTO.getQuantity(), totalPrice, member, LocalDateTime.now());

            orderRepository.save(order);
        }
    }
}
