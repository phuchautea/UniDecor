package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uni.decor.entity.ProductVariant;

import java.util.List;

@Repository
public interface IProductVariantRepository extends JpaRepository<ProductVariant, Long> {

}
