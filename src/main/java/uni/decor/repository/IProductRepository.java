package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.decor.entity.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
}
