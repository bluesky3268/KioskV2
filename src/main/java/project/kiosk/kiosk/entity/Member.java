package project.kiosk.kiosk.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String memberId;
    private String password;
    private String location;
    private LocalDateTime regDate;
    private String role;
    private String thumbImg;

    public Member(String memberId, String password, String location, LocalDateTime regDate, String role, String thumbImg) {
        this.memberId = memberId;
        this.password = password;
        this.location = location;
        this.regDate = regDate;
        this.role = role;
        this.thumbImg = thumbImg;
    }
}
