package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.entity.Role;

import javax.validation.constraints.NotNull;

@Getter
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
    @NotNull
    private String location;
    @NotNull
    private Role role;
    @NotNull
    private MultipartFile thumbImg;

    // 관리자 등록용 생성자
    public MemberJoinDTO(String id, String password, String passwordConfirm, Role role) {
        this.id = id;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.role = role;
    }
}
