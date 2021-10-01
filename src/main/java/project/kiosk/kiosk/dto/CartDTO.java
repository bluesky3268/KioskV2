package project.kiosk.kiosk.dto;

import lombok.Getter;
import lombok.Setter;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
import project.kiosk.kiosk.entity.Item;

@Getter
@Setter
public class CartDTO {
    private ItemResponseDto item;
    private int quantity;
}
