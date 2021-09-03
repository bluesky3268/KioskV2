package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
public class MemberJoinDTO {

    @NotNull
    private String memberId;
    @NotNull
    private String password;
    @NotNull
    private String passwordConfirm;
    @NotNull
    private String location;
    @NotNull
    private String role;
    @NotNull
    private MultipartFile thumbImg;

}
