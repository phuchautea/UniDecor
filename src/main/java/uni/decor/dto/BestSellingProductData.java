package uni.decor.dto;

public class BestSellingProductData {
    private long id;
    private String productName;
    private String variantName;
    private int soldQuantity;

    public BestSellingProductData(long id, String productName, String variantName, int soldQuantity) {
        this.id = id;
        this.productName = productName;
        this.variantName = variantName;
        this.soldQuantity = soldQuantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
