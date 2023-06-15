package uni.decor.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uni.decor.common.CustomLogger;
import uni.decor.dto.CreateProductVariantRequest;
import uni.decor.entity.Product;
import uni.decor.entity.ProductVariant;
import uni.decor.service.CategoryService;
import uni.decor.service.ProductService;
import uni.decor.service.ProductVariantService;
import uni.decor.service.UploadService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ManageProductController {
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
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product/add";
    }
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product, BindingResult result, Model model, @ModelAttribute CreateProductVariantRequest createProductVariantRequest) throws IOException {


        model.addAttribute("product", product);
        if(result.hasErrors()){
            return "admin/product/add";
        }
        productService.addProduct(product);
        List<String> variantNames = createProductVariantRequest.getVariant_name();
        List<Integer> variantQuantity = createProductVariantRequest.getVariant_quantity();
        List<String> variantPrices = createProductVariantRequest.getVariant_price();
        List<String> variantDiscountPrices = createProductVariantRequest.getVariant_discount_price();
        List<String> variantDescriptions = createProductVariantRequest.getVariant_description();
        List<String> variantImages = createProductVariantRequest.getVariant_image();
        CustomLogger.info("variantNames: "+variantNames);
        CustomLogger.info("variantQuantity: " + variantQuantity);
        CustomLogger.info("variantPrices: " + variantPrices);
        CustomLogger.info("variantDiscountPrices: " + variantDiscountPrices);
        CustomLogger.info("variantDescriptions: " + variantDescriptions);
        CustomLogger.info("variantImages: " + variantImages);
        // Lưu danh sách các giá trị vào cơ sở dữ liệu
        for (int i = 0; i < variantNames.size(); i++) {
            double price = Double.parseDouble(variantPrices.get(i).replaceAll(",", ""));
            double discount_price = Double.parseDouble(variantDiscountPrices.get(i).replaceAll(",", ""));

            ProductVariant variant = new ProductVariant();
            variant.setName(variantNames.get(i));
            variant.setStock(variantQuantity.get(i));
            variant.setPrice(price);
            variant.setDiscountPrice(discount_price);
            variant.setImage(variantImages.get(i));
            variant.setProduct(product);

            productVariantService.add(variant);
        }
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
}
