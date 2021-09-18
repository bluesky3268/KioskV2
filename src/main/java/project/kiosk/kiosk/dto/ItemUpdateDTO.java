package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.entity.UploadFile;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ItemUpdateDTO {

    private Integer price;

    private MultipartFile img;

    private boolean isSoldOut;

    // 테스트용 생성자
    public ItemUpdateDTO(Integer price, boolean isSoldOut) {
        this.price = price;
        this.isSoldOut = isSoldOut;
    }
}
