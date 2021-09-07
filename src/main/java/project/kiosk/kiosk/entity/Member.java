package project.kiosk.kiosk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private String loginId;

    @NotNull
    private String password;

    @Nullable
    private String location;

    @NotNull
    private LocalDateTime regDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Nullable
    @OneToOne
    private UploadFile thumbImg;

    // 관리자 등록용 생성자


    public Member(String loginId, String password, LocalDateTime regDate, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.regDate = regDate;
        this.role = role;
    }

    public Member(String loginId, String password, @Nullable String location, LocalDateTime regDate, Role role, @Nullable UploadFile thumbImg) {
        this.loginId = loginId;
        this.password = password;
        this.location = location;
        this.regDate = regDate;
        this.role = role;
        this.thumbImg = thumbImg;
    }
}
