package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.decor.entity.Category;
import uni.decor.entity.ProductVariant;
@Repository
public interface IProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}
