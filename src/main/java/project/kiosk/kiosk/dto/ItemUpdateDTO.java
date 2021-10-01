package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.entity.UploadFile;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ItemUpdateDTO {

    private Integer price;

    private String isSoldOut;

    private MultipartFile img;

    public ItemUpdateDTO() {
    }

    // 테스트용 생성자
    public ItemUpdateDTO(Integer price, String isSoldOut) {
        this.price = price;
        this.isSoldOut = isSoldOut;
    }
}
