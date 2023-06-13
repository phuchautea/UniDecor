package uni.decor.controller;

import org.json.JSONException;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import uni.decor.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.json.JSONObject;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadImage {
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ResourceLoader resourceLoader;
    @PostMapping
//    public ResponseEntity<String> uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException, JSONException {
//        String name =uploadService.uploadImage(file);
//        JSONObject responseJson = new JSONObject();
//        responseJson.put("path", name);
//        return ResponseEntity.ok().body(responseJson.toString());
//    }
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) throws JSONException {
        // Kiểm tra xem tệp tin có tồn tại và không rỗng không
        JSONObject responseJson = new JSONObject();
        try {
        if (file == null || file.isEmpty()) {
            responseJson.put("message","Chưa chọn ảnh !!");
            return ResponseEntity.badRequest().body("Vui lòng chọn một tệp tin ảnh.");
        }
        // Kiểm tra định dạng tệp tin
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!isSupportedImageFormat(fileExtension)) {
            responseJson.put("message","Chúng tôi chỉ hỗ trợ tệp tin có định dạng jpg, jpeg, png, webp");
            return ResponseEntity.badRequest().body(responseJson.toString());
        }
        // Tiếp tục xử lý tệp tin ảnh nếu đúng định dạng

            String name = uploadService.uploadImage(file);


//            responseJson.put("path", "/static/" + name);
            responseJson.put("path", "/"+ name);

            return ResponseEntity.ok().body(responseJson.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            responseJson.put("message", "Lỗi xử lý JSON: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseJson.toString());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi trong quá trình tải lên tệp tin ảnh.");
        }
    }
    private String getFileExtension(String filename) {
        return StringUtils.getFilenameExtension(filename);
    }
    private boolean isSupportedImageFormat(String fileExtension) {
        // Danh sách các định dạng hình ảnh được hỗ trợ (có thể mở rộng theo nhu cầu của bạn)
        String[] supportedFormats = {"jpg", "jpeg", "png", "webp"};
        for (String format : supportedFormats) {
            if (format.equalsIgnoreCase(fileExtension)) {
                return true;
            }
        }
        return false;
    }
}
