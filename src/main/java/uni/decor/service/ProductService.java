package uni.decor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.entity.Product;
import uni.decor.repository.IProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepository;
    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
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
}
