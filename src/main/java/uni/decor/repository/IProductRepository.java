package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uni.decor.entity.Product;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop10ByOrderByCreatedAtDesc();
    @Query("SELECT p FROM Product p JOIN p.productVariants pv GROUP BY p.id ORDER BY SUM(pv.soldQuantity) DESC")
    List<Product> findTop10BySoldQuantity();
    Product findBySlug(String slug);
}
