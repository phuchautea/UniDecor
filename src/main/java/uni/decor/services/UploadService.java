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
                // Lưu trữ tệp tin ảnh trên server
                int lastDotIndex = file.getOriginalFilename().indexOf(".");
                String valueBeforeDot = file.getOriginalFilename().substring(0,lastDotIndex);
                String Fileextensiton = getFileExtension (file.getOriginalFilename());

                String fileName = generateUniqueFileName(valueBeforeDot);
                System.out.println(valueBeforeDot);
                String uploadDir = imageStorage; // Thay đổi thành đường dẫn thư mục lưu trữ thực tế trên server

                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs(); // Tạo thư mục lưu trữ nếu không tồn tại
                }

                File serverFile = new File(uploadPath.getAbsolutePath() + File.separator + fileName + Fileextensiton );
                file.transferTo(serverFile);

                // Lưu đường dẫn hình ảnh vào đối tượng Book
                return serverFile.getName();
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi khi lưu trữ tệp tin
            }
        }
        return  null;
    }
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }

    private String generateUniqueFileName(String filename) {
        String uniqueFileName = filename+"_"+UUID.randomUUID().toString();
        return uniqueFileName;
    }
}