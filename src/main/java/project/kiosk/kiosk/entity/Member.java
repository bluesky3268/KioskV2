package project.kiosk.kiosk.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String memberId;
    private String password;
    private String location;
    private LocalDateTime regDate;
    private String role;
    @OneToOne
    private UploadFile thumbImg;

    public Member( String memberId, String password, String location, LocalDateTime regDate, String role, UploadFile thumbImg) {
        this.memberId = memberId;
        this.password = password;
        this.location = location;
        this.regDate = regDate;
        this.role = role;
        this.thumbImg = thumbImg;
    }
}
