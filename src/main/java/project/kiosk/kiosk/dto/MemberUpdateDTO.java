package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.entity.UploadFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDTO {

    private String password;
    private String location;
    private String role;
    private MultipartFile img;

    // 테스트용 생성자
    public MemberUpdateDTO(String password, String location, String role) {
        this.password = password;
        this.location = location;
        this.role = role;
    }
}
