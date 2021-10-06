package project.kiosk.kiosk.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.entity.UploadFile;
import project.kiosk.kiosk.repository.FileRepository;
import project.kiosk.kiosk.service.FileService;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public UploadFile addFile(UploadFile uploadFile) {
        UploadFile savedFile = fileRepository.save(uploadFile);
        return savedFile;
    }
}
