package project.kiosk.kiosk.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.entity.Role;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinDTO {

    @NotNull
    private String id;

    @NotNull
    private String password;

    @NotNull
    private String passwordConfirm;

    private String location;

    @NotNull
    private String role;

    @NotNull
    private MultipartFile thumbImg;

    // 관리자 등록용 생성자
    public MemberJoinDTO(String id, String password, String passwordConfirm, String role) {
        this.id = id;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.role = role;
    }
}
