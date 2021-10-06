package project.kiosk.kiosk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.kiosk.kiosk.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByMemberNo(Long memberNo, Pageable pageable);

}
