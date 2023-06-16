package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uni.decor.dto.BestSellingProductData;
import uni.decor.dto.PaymentMethodData;
import uni.decor.dto.SalesData;
import uni.decor.entity.Order;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Order findByCode(String code);
    @Query("SELECT new uni.decor.dto.SalesData(DATE_FORMAT(o.createdAt, '%M'), SUM(o.totalPrice)) " +
            "FROM Order o " +
            "WHERE YEAR(o.createdAt) = ?1 " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt) " +
            "ORDER BY YEAR(o.createdAt) ASC, MONTH(o.createdAt) ASC")
    List<SalesData> getSalesData(int year);

    @Query("SELECT new uni.decor.dto.PaymentMethodData(o.paymentMethod, COUNT(*)) " +
            "FROM Order o " +
            "GROUP BY o.paymentMethod")
    List<PaymentMethodData> getPaymentMethodData();

    @Query("SELECT new uni.decor.dto.BestSellingProductData(pv.id, p.name, pv.name, pv.soldQuantity) " +
            "FROM ProductVariant pv " +
            "JOIN pv.product p " +
            "ORDER BY pv.soldQuantity DESC")
    List<BestSellingProductData> getBestSellingProducts();
}
