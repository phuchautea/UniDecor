package uni.decor.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uni.decor.service.UploadService;

import java.io.IOException;
@RestController
@RequestMapping("/upload")
public class UploadImageRestController {
    @Autowired
    private UploadService uploadService;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) throws JSONException {
        JSONObject responseJson = new JSONObject();
        try {

            // Kiểm tra xem tệp tin có tồn tại và không rỗng không
            if (file == null || file.isEmpty()) {
                return ResponseEntity.ok().body("Vui lòng chọn một tệp tin ảnh.");
            }

            // Kiểm tra định dạng tệp tin
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = uploadService.getFileExtension(originalFilename);
            if (!isSupportedImageFormat(fileExtension)) {
                responseJson.put("message","Chúng tôi chỉ hỗ trợ tệp tin có định dạng jpg, jpeg, png, webp");
                return ResponseEntity.badRequest().body(responseJson.toString());
            }

            // Tiếp tục xử lý tệp tin ảnh nếu đúng định dạng
            String name = uploadService.uploadImage(file);
            responseJson.put("path","\\"+ name);
            return ResponseEntity.ok().body(responseJson.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            responseJson.put("message", "Lỗi xử lý JSON: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseJson.toString());
        }
    }

    private boolean isSupportedImageFormat(String fileExtension) {
        // Danh sách các định dạng hình ảnh được hỗ trợ
        String[] supportedFormats = {"jpg", "jpeg", "png", "webp"};
        for (String format : supportedFormats) {
            if (format.equalsIgnoreCase(fileExtension)) {
                return true;
            }
        }
        return false;
    }

}
