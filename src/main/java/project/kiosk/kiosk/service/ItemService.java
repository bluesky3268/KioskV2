package project.kiosk.kiosk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.ItemUpdateDTO;
import project.kiosk.kiosk.entity.Item;

import java.util.List;

public interface ItemService {

    Item addItem(ItemAddDTO itemAddDTO, String memberId);

    Item editItem(Long no, ItemUpdateDTO itemUpdate);

    void deleteItem(Long no);

    List<Item> findItems(Long memberNo);

    Item findItem(Long no);

    Item findItemByItemName(String itemName);

    Item findItemDetail(Long no);

    Page<Item> findByMemberNoWithPage(Long no, Pageable pageable);

    Page<Item> findAll(Pageable pageable);

}
