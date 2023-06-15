package uni.decor.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uni.decor.entity.Product;
import uni.decor.entity.ProductVariant;
import uni.decor.service.CategoryService;
import uni.decor.service.ProductService;
import uni.decor.service.ProductVariantService;
import uni.decor.service.UploadService;
import uni.decor.entity.ProductVariant;
import uni.decor.service.ProductVariantService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/admin/products")
public class ProductController {
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductVariantService productVariantService;
    @GetMapping
    public String showAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/product/list";
    }
    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
//        List<ProductVariant> productVariants = new ArrayList<>();
//        model.addAttribute("productVariants", productVariants);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product/add";
    }
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product, BindingResult result, Model model) throws IOException {

//        model.addAttribute("productVariant",productVariant);
        if(result.hasErrors()){
            model.addAttribute("product", product);
            return "admin/product/add";
        }


        productService.addProduct(product);
//        productVariantService.addProductVariant(productVariant);
        return "redirect:/admin/products";
    }


    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Product editProduct = productService.getProductById(id);
        model.addAttribute("categories", categoryService.getAllCategories());
        if(editProduct != null) {
            model.addAttribute("product", editProduct);
            return "admin/product/edit";
        }else{
            return "not-found";
        }
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute("product") Product updatedProduct, @RequestParam Long category) {
        productService.updateProduct(updatedProduct, category);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProductForm(@PathVariable("id") Long id, Model model) {
        productService.deleteCProduct(id);
        return "redirect:/admin/products";
    }

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
