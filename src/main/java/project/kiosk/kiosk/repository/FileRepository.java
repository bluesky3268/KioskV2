package project.kiosk.kiosk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.kiosk.kiosk.entity.UploadFile;

@Repository
public interface FileRepository extends JpaRepository<UploadFile, Long> {

}
