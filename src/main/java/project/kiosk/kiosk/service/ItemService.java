package project.kiosk.kiosk.service;

import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.entity.Item;

public interface ItemService {

    Item addItem(ItemAddDTO itemAddDTO);

    Item editItem(String itemId);

    void deleteItem(String itemId);

}
