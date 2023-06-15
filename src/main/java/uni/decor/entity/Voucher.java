package uni.decor.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public Double getMinimumSpent() {
        return minimumSpent;
    }

    public void setMinimumSpent(Double minimumSpent) {
        this.minimumSpent = minimumSpent;
    }

    public Double getDiscountMaxValue() {
        return discountMaxValue;
    }

    public void setDiscountMaxValue(Double discountMaxValue) {
        this.discountMaxValue = discountMaxValue;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public Integer getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(Integer maxUsage) {
        this.maxUsage = maxUsage;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public List<VoucherUsage> getVoucherUsages() {
        return voucherUsages;
    }

    public void setVoucherUsages(List<VoucherUsage> voucherUsages) {
        this.voucherUsages = voucherUsages;
    }

    @OneToMany(mappedBy = "voucher")
    private List<VoucherUsage> voucherUsages = new ArrayList<>();

}
