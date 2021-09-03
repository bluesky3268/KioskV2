package project.kiosk.kiosk.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class MemberLoginDTO {

    @NotNull
    private String loginId;
    @NotNull
    private String loginPwd;
}
