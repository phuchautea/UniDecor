package uni.decor.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadService {
    @Value("${myapp.image-storage}")
    private String  imageStorage;
    public String uploadImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try {

                String fileName = generateUniqueFileName(file.getOriginalFilename());
                String uploadDir = imageStorage; // Path cấu hình ở application

                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs(); // Tạo thư mục lưu trữ nếu không tồn tại
                }

                File serverFile = new File(uploadPath.getAbsolutePath() + File.separator + fileName);
                file.transferTo(serverFile);

                // trả về Name của file upload
                return serverFile.getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }
    private String generateUniqueFileName(String filename) {
        String uniqueFileName = filename+"_"+UUID.randomUUID().toString() ; // tạo uniquename bằng filename(original)+random UUID
        return uniqueFileName;
    }
}