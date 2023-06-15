package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.decor.entity.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Order findByCode(String code);
}
