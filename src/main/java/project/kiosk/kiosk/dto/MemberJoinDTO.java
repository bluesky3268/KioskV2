package project.kiosk.kiosk.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

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
    private String thumbImg;

}
