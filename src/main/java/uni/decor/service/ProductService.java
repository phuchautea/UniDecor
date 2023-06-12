package uni.decor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.entity.Category;
import uni.decor.entity.Product;
import uni.decor.repository.ICategoryRepository;
import uni.decor.repository.IProductRepository;
import uni.decor.utils.SlugUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    LocalDateTime now = LocalDateTime.now();
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }
    public Product getProductById(Long id)
    {
        return productRepository.findById(id).orElse(null);
    }
    public Product save(Product product)
    {
        return productRepository.save(product);
    }

    public void addProduct(Product product)
    {
        String slug = SlugUtils.createSlug(product.getName());
        product.setSlug(slug);
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        save(product);
    }
    public void deleteCProduct(Long id)
    {
        productRepository.deleteById(id);
    }
    public void updateProduct(Product product, Long categoryId)
    {
        Product existingProduct = productRepository.findById(product.getId()).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if(existingProduct != null)
        {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCategory(category);
            existingProduct.setUpdatedAt(now);
            save(existingProduct);

        }
    }

}

