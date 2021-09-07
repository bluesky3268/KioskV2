package project.kiosk.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.entity.UploadFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDTO {

    private String password;
    private String location;
    private Role role;
    private UploadFile thumbImg;

}
