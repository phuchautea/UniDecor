package uni.decor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uni.decor.entity.Category;
import uni.decor.entity.Product;
import uni.decor.entity.ProductVariant;
import uni.decor.service.ProductService;
import uni.decor.service.CategoryService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/collections")
public class CollectionController {
    @Autowired
    private ProductService productService;
    private CategoryService categoryService;
    @GetMapping("/all")
    public String showAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size, Model model) {
        Page<Product> products = productService.getPagingProducts(page,size);
        model.addAttribute("products", products);
        model.addAttribute("totalProduct",productService.totalProduct());
        model.addAttribute("totalPages",products.getTotalPages());
        return "category/collections";
    }


    @GetMapping("/sort/{sortBy}")
    public String sortProducts(@PathVariable String sortBy, Model model)
    {
        List<Product> products = productService.getAllProducts();
        switch (sortBy) {
            case "price-ascending":
                products = products.stream()
                        .sorted(Comparator.comparingDouble(product -> {
                            Optional<ProductVariant> firstVariant = Optional.ofNullable(product.getProductVariants())
                                    .flatMap(variants -> variants.stream().findFirst());
                            return firstVariant.map(ProductVariant::getPrice).orElse(0.0);
                        }))
                        .collect(Collectors.toList());
                break;
            case "name-ascending":
                products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
                break;
            case "name-descending":
                products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
                Collections.reverse(products);
                break;
            case "newest":
                products.sort(Comparator.comparing(Product::getCreatedAt));
                Collections.reverse(products);
                break;
            case "oldest":
                products.sort(Comparator.comparing(Product::getCreatedAt));
                break;
            default:
                break;

        }
        model.addAttribute("products", products);
        model.addAttribute("totalProduct",productService.totalProduct());
        return "category/collections";
    }
}
