package uni.decor.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_code", nullable = false)
    private String discountCode;

    @Column(name = "discount_value", nullable = false)
    private String discountValue;

    @Column(name = "minimum_spent")
    private Double minimumSpent;

    @Column(name = "discount_max_value")
    private Double discountMaxValue;

    @Column(name = "valid_from", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime validFrom;

    @Column(name = "valid_to", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime validTo;

    @Column(name = "max_usage")
    private Integer maxUsage;

    @Column(name = "used_count")
    private Integer usedCount;


    @OneToMany(mappedBy = "voucher")
    private List<VoucherUsage> voucherUsages = new ArrayList<>();

}
