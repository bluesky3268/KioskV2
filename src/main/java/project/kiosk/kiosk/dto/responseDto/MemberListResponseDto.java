package project.kiosk.kiosk.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.entity.UploadFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberListResponseDto {

    private Long no;
    private String id;
    private String location;
    private LocalDateTime regDate;
    private Role role;
    private UploadFile thumbImg;
    private String savedImgName;
    private String originalImgName;

    public MemberListResponseDto(Long no, String id, String location, LocalDateTime regDate, Role role, UploadFile thumbImg, String savedImgName, String originalImgName) {
        this.no = no;
        this.id = id;
        this.location = location;
        this.regDate = regDate;
        this.role = role;
        this.thumbImg = thumbImg;
        this.savedImgName = savedImgName;
        this.originalImgName = originalImgName;
    }
}
