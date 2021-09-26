package project.kiosk.kiosk.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uploadFile_no")
    private Long no;

    private String originalName;
    private String saveName;

    public UploadFile(String originalName, String saveName) {
        this.originalName = originalName;
        this.saveName = saveName;
    }
}
