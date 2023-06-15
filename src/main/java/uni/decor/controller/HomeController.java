package uni.decor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uni.decor.entity.Category;
import uni.decor.entity.Product;
import uni.decor.service.CategoryService;
import uni.decor.service.ProductService;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @GetMapping("/")
    public String index(Model model){
        List<Category> categories = categoryService.getAll();
        List<Product> newestProducts = productService.getNewestProducts();
        List<Product> bestSellingProducts = productService.getBestSellingProducts();
        model.addAttribute("categories", categories);
        model.addAttribute("newestProducts", newestProducts);
        model.addAttribute("bestSellingProducts", bestSellingProducts);
        return "index";
    }
}
