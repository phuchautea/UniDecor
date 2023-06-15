package uni.decor.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uni.decor.entity.Product;
import uni.decor.service.CategoryService;
import uni.decor.service.ProductService;
import uni.decor.service.UploadService;
import uni.decor.entity.ProductVariant;
import uni.decor.service.ProductVariantService;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{slug}")
    public String getBySlug(@PathVariable("slug") String slug, Model model, HttpServletRequest request) {
        Product product = productService.getProductBySlug(slug);
        if(product != null) {
            String currentUrl = request.getRequestURL().toString();
            model.addAttribute("product", product);
            model.addAttribute("currentUrl", currentUrl);
            model.addAttribute("productVariantList", product.getProductVariants());
            return "product";
        }else{
            return "redirect:/";
        }
    }
    @GetMapping("/search")
    public String searchProduct(Model model, @RequestParam("keyword") String keyword) {
        var items = productService.getAllProducts().stream()
                .filter(m -> m.getName().toUpperCase().contains(keyword.toUpperCase())
                        || m.getDescription().toUpperCase().contains(keyword.toUpperCase()))
                .collect(Collectors.toList());

        if (keyword.isEmpty() || items.isEmpty()) {
            return "product/searchFailed";
        }

        model.addAttribute("products", items);
        model.addAttribute("keyword", keyword);
        return "product/search";
    }
}
