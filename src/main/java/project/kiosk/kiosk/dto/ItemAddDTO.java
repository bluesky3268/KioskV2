package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.entity.UploadFile;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ItemAddDTO {

    @NotNull
    private String itemName;
    @NotNull
    private Integer price;
    @NotNull
    private boolean isSoldOut;

    private MultipartFile img;

    private Long memberNo;

    // 테스트용 생성자
    public ItemAddDTO(String itemName, Integer price, boolean isSoldOut, Long memberNo) {
        this.itemName = itemName;
        this.price = price;
        this.isSoldOut = isSoldOut;
        this.memberNo = memberNo;
    }
}
