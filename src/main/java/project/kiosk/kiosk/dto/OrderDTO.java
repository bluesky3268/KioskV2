package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private String itemName;
    private int price;
    private int quantity;

    private int totalPrice;

}
