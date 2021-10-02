package project.kiosk.kiosk.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.entity.UploadFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Data
public class FileStore {

    /**
     * MultipartFile을 UploadFile 타입(원본이름, DB저장 이름)으로 변환, 지정된 경로에 파일을 저장
     * -> FileRepository를 통해서 DB에 UploadFile을 저장하여 파일 관리
     */


    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public UploadFile saveFile(MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String originalFilename = multipartFile.getOriginalFilename();

            int p = originalFilename.lastIndexOf(".");
            String extension = originalFilename.substring(p);

            String uuid = UUID.randomUUID().toString();
            String saveFileName = uuid + "." + extension;

            multipartFile.transferTo(new File(getFullPath(saveFileName)));
            return new UploadFile(originalFilename, saveFileName);
        }
        return null;

    }


}
