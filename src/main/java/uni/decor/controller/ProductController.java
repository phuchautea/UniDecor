package uni.decor.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uni.decor.entity.Product;
import uni.decor.entity.ProductVariant;
import uni.decor.service.ProductService;
import uni.decor.service.ProductVariantService;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductVariantService productVariantService;
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
}
