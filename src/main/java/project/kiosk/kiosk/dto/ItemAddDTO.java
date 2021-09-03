package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.kiosk.kiosk.entity.UploadFile;

@Getter
@AllArgsConstructor
public class ItemAddDTO {
    private String itemName;
    private Integer price;
    private boolean isSoldOut;
    private UploadFile img;
    private Integer memberId;
}
