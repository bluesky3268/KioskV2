package project.kiosk.kiosk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.kiosk.kiosk.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
