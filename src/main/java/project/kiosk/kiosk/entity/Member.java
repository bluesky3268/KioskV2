package project.kiosk.kiosk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import project.kiosk.kiosk.dto.responseDto.MemberListResponseDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long no;

    @NotNull
    private String id;

    @NotNull
    @JsonIgnore
    private String password;

    @Nullable
    private String location;

    @NotNull
    private LocalDateTime regDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Nullable
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploadFile_no")
    private UploadFile thumbImg;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Item> items = new ArrayList<>();

    // 관리자 등록용 생성자
    public Member(String id, String password, LocalDateTime regDate, Role role) {
        this.id = id;
        this.password = password;
        this.regDate = regDate;
        this.role = role;
    }

//    @Builder
    public Member(String id, String password, @Nullable String location, LocalDateTime regDate, Role role, @Nullable UploadFile thumbImg) {
        this.id = id;
        this.password = password;
        this.location = location;
        this.regDate = regDate;
        this.role = role;
        this.thumbImg = thumbImg;
    }


    @Override
    public String toString() {
        return "Member{" +
                "no=" + no +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", regDate=" + regDate +
                ", role=" + role +
                '}';
    }
}
