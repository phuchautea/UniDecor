package uni.decor.dto;

public class CartItemRequest {
    private int quantity;
    private Long productId;
    private Long productVariantId;

    public CartItemRequest() {
    }

    public CartItemRequest(int quantity, Long productId, Long productVariantId) {
        this.quantity = quantity;
        this.productId = productId;
        this.productVariantId = productVariantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(Long productVariantId) {
        this.productVariantId = productVariantId;
    }
}
