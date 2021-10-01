package project.kiosk.kiosk.service;

import project.kiosk.kiosk.entity.UploadFile;

import java.util.Optional;

public interface FileService {

    UploadFile addFile(UploadFile uploadFile);

}
