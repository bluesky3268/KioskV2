package project.kiosk.kiosk.dto.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiosk.kiosk.entity.UploadFile;

@Getter
@NoArgsConstructor
public class ItemResponseDto {

    private Long no;
    private String itemName;
    private Integer price;
    private UploadFile thumbImg;
    private boolean isSoldOut;
    private String memberId;

    public ItemResponseDto(Long no, String itemName, Integer price, UploadFile thumbImg, boolean isSoldOut, String memberId) {
        this.no = no;
        this.itemName = itemName;
        this.price = price;
        this.thumbImg = thumbImg;
        this.isSoldOut = isSoldOut;
        this.memberId = memberId;
    }
}
