package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.decor.entity.Voucher;

@Repository
public interface IVoucherRepository extends JpaRepository<Voucher, Long> {

}