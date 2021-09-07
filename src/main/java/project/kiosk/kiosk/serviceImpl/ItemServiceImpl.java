package project.kiosk.kiosk.serviceImpl;

import org.springframework.stereotype.Service;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

    @Override
    public Item addItem(ItemAddDTO itemAddDTO) {
        return null;
    }

    @Override
    public Item editItem(String itemId) {
        return null;
    }

    @Override
    public void deleteItem(String itemId) {

    }
}
