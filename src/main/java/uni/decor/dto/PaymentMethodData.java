package uni.decor.dto;

public class PaymentMethodData {
    private String paymentMethod;
    private int total;

    public PaymentMethodData(String paymentMethod, int total) {
        this.paymentMethod = paymentMethod;
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
