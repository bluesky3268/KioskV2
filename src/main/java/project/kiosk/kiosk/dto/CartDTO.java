package project.kiosk.kiosk.dto;

import lombok.Getter;
import lombok.Setter;
import project.kiosk.kiosk.entity.Item;

@Getter
@Setter
public class CartDTO {
    private Item item;
    private int quantity;
}
