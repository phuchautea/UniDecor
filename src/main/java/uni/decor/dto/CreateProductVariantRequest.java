package uni.decor.dto;

import java.util.List;

public class CreateProductVariantRequest {
    private List<String> variant_name;
    private List<Integer> variant_quantity;
    private List<String> variant_price;
    private List<String> variant_discount_price;
    private List<String> variant_description;
    private List<String> variant_image;

    public List<String> getVariant_name() {
        return variant_name;
    }

    public void setVariant_name(List<String> variant_name) {
        this.variant_name = variant_name;
    }

    public List<Integer> getVariant_quantity() {
        return variant_quantity;
    }

    public void setVariant_quantity(List<Integer> variant_quantity) {
        this.variant_quantity = variant_quantity;
    }

    public List<String> getVariant_price() {
        return variant_price;
    }

    public void setVariant_price(List<String> variant_price) {
        this.variant_price = variant_price;
    }

    public List<String> getVariant_discount_price() {
        return variant_discount_price;
    }

    public void setVariant_discount_price(List<String> variant_discount_price) {
        this.variant_discount_price = variant_discount_price;
    }

    public List<String> getVariant_description() {
        return variant_description;
    }

    public void setVariant_description(List<String> variant_description) {
        this.variant_description = variant_description;
    }

    public List<String> getVariant_image() {
        return variant_image;
    }

    public void setVariant_image(List<String> variant_image) {
        this.variant_image = variant_image;
    }
}
