package uni.decor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uni.decor.entity.Category;
import uni.decor.entity.Product;
import uni.decor.repository.ICategoryRepository;
import uni.decor.repository.IProductRepository;
import uni.decor.util.SlugUtils;

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
    public List<Product> findByCategorySlug(String categorySlug)
    {
        return productRepository.findByCategorySlug(categorySlug);
    }

    public Product getProductBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    public List<Product> getNewestProducts() {
        return productRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<Product> getBestSellingProducts() {
        return productRepository.findTop10BySoldQuantity();
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
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
            String slug = SlugUtils.createSlug(product.getName());
            product.setSlug(slug);
            existingProduct.setImage(product.getImage());
            existingProduct.setCategory(category);
            existingProduct.setUpdatedAt(now);
            save(existingProduct);

        }
    }

    public Page<Product> getPagingProducts( int page, int size)
    {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findAll(pageRequest);
    }
    public List<Product> getProductByCategorySlug(String categorySlug)
    {
        return productRepository.findByCategorySlug(categorySlug);
    }
    public Long totalProduct(){
        return productRepository.count();
    }
    public Long totalProductByCategorySlug(String categorySlug){
        return getProductByCategorySlug(categorySlug).stream().count();
    }
}