package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.entity.UploadFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemAddDTO {

    private String memberId;

    @NotNull
    private String itemName;

    @NotNull
    private Integer price;

    @NotNull
    private String isSoldOut;

    private MultipartFile img;

    // 테스트용 생성자
    public ItemAddDTO(String memberId, String itemName, Integer price, String isSoldOut) {
        this.memberId = memberId;
        this.itemName = itemName;
        this.price = price;
        this.isSoldOut = isSoldOut;
    }

    @Override
    public String toString() {
        return "ItemAddDTO{" +
                "memberId='" + memberId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", isSoldOut='" + isSoldOut + '\'' +
                ", img=" + img +
                '}';
    }
}
