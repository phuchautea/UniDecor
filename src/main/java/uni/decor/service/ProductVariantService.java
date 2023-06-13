package uni.decor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.entity.Category;
import uni.decor.entity.Product;
import uni.decor.entity.ProductVariant;
import uni.decor.repository.IProductRepository;
import uni.decor.repository.IProductVariantRepository;
import uni.decor.utils.SlugUtils;

import java.util.List;

@Service
public class ProductVariantService {
    @Autowired
    private IProductVariantRepository productVariantRepository;
    @Autowired
    private IProductRepository productRepository;
    public List<ProductVariant> getAllProductVariants()
    {
        return productVariantRepository.findAll();
    }
    public ProductVariant getProductVariantId(Long id)
    {
        return productVariantRepository.findById(id).orElse(null);
    }
    public ProductVariant save(ProductVariant productVariant)
    {
        return productVariantRepository.save(productVariant);
    }
    public void addProductVariant(ProductVariant productVariant)
    {
        String slug = SlugUtils.createSlug(productVariant.getName());
        productVariant.setSlug(slug);
        save(productVariant);
    }
    public void deleteProductVariant(Long id)
    {
        productVariantRepository.deleteById(id);
    }
    public void updateProductVariant(ProductVariant productVariant, Long productId)
    {
        ProductVariant existingProductVariant = productVariantRepository.findById(productVariant.getId()).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        if(existingProductVariant != null)
        {

            existingProductVariant.setName(productVariant.getName());
            String slug = SlugUtils.createSlug(productVariant.getName());
            existingProductVariant.setSlug(slug);
            existingProductVariant.setImage(productVariant.getImage());
            existingProductVariant.setProduct(productVariant.getProduct());
            existingProductVariant.setDiscountPrice(productVariant.getDiscountPrice());
            existingProductVariant.setStock(productVariant.getStock());
            existingProductVariant.setPrice(productVariant.getPrice());
            existingProductVariant.setSoldQuantity(productVariant.getSoldQuantity());
            existingProductVariant.setOrderVariants(productVariant.getOrderVariants());
            save(existingProductVariant);

        }
    }
}
