package uni.decor.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;


@Controller
@RequestMapping("")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/product/{slug}")
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

//    @GetMapping("/sort/{sortBy}")
//    public String sortProducts(@PathVariable String sortBy, Model model)
//    {
//        List<Product> products = productService.getAllProducts();
//        switch (sortBy) {
//            case "price-ascending":
//                products.sort(Comparator.comparingDouble(product -> Optional.ofNullable(product.getPrice()).orElse(0.0)));
//                break;
//            case "name-ascending":
//                products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
//                break;
//            case "name-descending":
//                products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
//                Collections.reverse(products);
//                break;
//            default:
//                break;
//
//        }
//        model.addAttribute("products", products);
//        return "product";
//    }
}
