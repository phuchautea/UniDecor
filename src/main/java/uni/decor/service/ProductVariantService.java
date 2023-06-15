package uni.decor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.entity.Product;
import uni.decor.entity.ProductVariant;
import uni.decor.repository.IProductVariantRepository;
import uni.decor.util.SlugUtils;

import java.util.List;
import java.util.Optional;
@Service
public class ProductVariantService {
    @Autowired
    private IProductVariantRepository productVariantRepository;
    public ProductVariant getById(Long id) {
        return productVariantRepository.findById(id).orElse(null);
    }
    public ProductVariant save(ProductVariant productVariant)
    {
        return productVariantRepository.save(productVariant);
    }
    public void add(ProductVariant productVariant)
    {
        String slug = SlugUtils.createSlug(productVariant.getName());
        productVariant.setSlug(slug);
        productVariant.setSoldQuantity(0);
        save(productVariant);
    }
}
