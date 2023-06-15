package uni.decor.dto;

public class OrderRequest {
    private String billing_name;
    private String billing_address;
    private String province_value;
    private String district_value;
    private String ward_value;
    private String billing_phone;
    private String billing_email;
    private String payment;

    public String getBilling_name() {
        return billing_name;
    }

    public void setBilling_name(String billing_name) {
        this.billing_name = billing_name;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
    }

    public String getProvince_value() {
        return province_value;
    }

    public void setProvince_value(String province_value) {
        this.province_value = province_value;
    }

    public String getDistrict_value() {
        return district_value;
    }

    public void setDistrict_value(String district_value) {
        this.district_value = district_value;
    }

    public String getWard_value() {
        return ward_value;
    }

    public void setWard_value(String ward_value) {
        this.ward_value = ward_value;
    }

    public String getBilling_phone() {
        return billing_phone;
    }

    public void setBilling_phone(String billing_phone) {
        this.billing_phone = billing_phone;
    }

    public String getBilling_email() {
        return billing_email;
    }

    public void setBilling_email(String billing_email) {
        this.billing_email = billing_email;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
