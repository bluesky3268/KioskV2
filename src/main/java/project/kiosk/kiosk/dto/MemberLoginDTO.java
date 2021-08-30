package project.kiosk.kiosk.dto;

import com.sun.istack.NotNull;
import lombok.Getter;

@Getter
public class MemberLoginDTO {

    @NotNull
    private String loginId;
    @NotNull
    private String loginPwd;
}
