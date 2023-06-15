package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.decor.entity.OrderVariant;

@Repository
public interface IOrderVariantRepository extends JpaRepository<OrderVariant, Long> {
}
