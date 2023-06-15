package uni.decor.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uni.decor.entity.Product;
import uni.decor.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchRestController {
    @Autowired
    private ProductService productService;
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("keyword") String keyword) throws JSONException { // dùng chấm hỏi chỗ <?> để cho
        // kiểu đại diện vì phải trả về message và list book!
        // Xử lý tìm kiếm và trả về kết quả dưới dạng List<Book>
        List<Product> searchResults = performSearch(keyword);
        if (searchResults.isEmpty()) {
            JSONObject responseJson = new JSONObject();
            responseJson.put("message", "Không tìm thấy sản phẩm cần search!");

            return ResponseEntity.badRequest().body(responseJson.toString());
        }
        JSONArray jsonArr = new JSONArray();
        for (Product product : searchResults) {
            JSONObject productJson = new JSONObject();
            productJson.put("name", product.getName());
            productJson.put("description", product.getDescription());
            productJson.put("image", product.getImage());
            productJson.put("slug", product.getSlug());
            jsonArr.put(productJson);
        }
        return ResponseEntity.ok(jsonArr.toString());
    }

    public List<Product> performSearch(String keyword) {
        // Thực hiện tìm kiếm trong danh sách đối tượng
        List<Product> searchResults = productService.getAllProducts().stream()
                .filter(m -> m.getName().toUpperCase().contains(keyword.toUpperCase())
                        || m.getDescription().toUpperCase().contains(keyword.toUpperCase()))
                .collect(Collectors.toList());

        return searchResults;
    }
}
