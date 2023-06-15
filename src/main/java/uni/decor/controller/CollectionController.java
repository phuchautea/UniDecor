package uni.decor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uni.decor.entity.Product;
import uni.decor.service.ProductService;

import java.util.List;

@Controller
@RequestMapping("/collections")
public class CollectionController {
    @Autowired
    private ProductService productService;
    @GetMapping("/all")
    public String showAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size, Model model) {
        Page<Product> products = productService.getPagingProducts(page,size);
        model.addAttribute("products", products);
        model.addAttribute("totalProduct",productService.totalProduct());
        model.addAttribute("totalPages",products.getTotalPages());
        return "category/collections";
    }
}
