package uni.decor.dto;

import lombok.Data;

@Data
public class MomoResponse {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private double amount;
    private long responseTime;
    private String message;
    private String orderInfo;
    private int resultCode;
    private String payUrl;
}
