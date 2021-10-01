package project.kiosk.kiosk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Role;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByItemName(String itemName);

    Item findByNo(Long no);

    List<Item> findAllByMemberNo(Long memberNo);

    Page<Item> findByMemberNo(Long memberNo, Pageable pageable);

    Page<Item> findAllByMemberRoleLike(Role role, Pageable pageable);

//    Page<Item> findAllItems(Pageable pageable);


}
