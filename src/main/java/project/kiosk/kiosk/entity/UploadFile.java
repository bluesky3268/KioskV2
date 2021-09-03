package project.kiosk.kiosk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UploadFile {
    @Id
    private Long id;
    private String originalName;
    private String saveName;

    public UploadFile(String originalName, String saveName) {
        this.originalName = originalName;
        this.saveName = saveName;
    }
}
