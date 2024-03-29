package uni.decor.service;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uni.decor.common.CustomLogger;
import uni.decor.dto.MomoRequest;
import uni.decor.encryption.MomoSecurity;
import uni.decor.entity.Order;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MomoPaymentService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${uniDecor.momo.endPoint}")
    private String endPoint;
    @Value("${uniDecor.momo.partnerCode}")
    private String partnerCode;
    @Value("${uniDecor.momo.accessKey}")
    private String accessKey;
    @Value("${uniDecor.momo.secretKey}")
    private String secretKey;
    @Value("${uniDecor.momo.ipnUrl}")
    private String ipnUrl;
    @Value("${uniDecor.momo.redirectUrl}")
    private String redirectUrl;

    public String process(int total_price, HttpSession session) throws NoSuchAlgorithmException, InvalidKeyException {
        Order order = (Order) session.getAttribute("order");
        String requestType = "captureWallet";
        String orderInfo = "Thanh toán đơn hàng";
        CustomLogger.info("Amount: "+total_price);
        String amount = String.valueOf(total_price);
        String orderId = java.util.UUID.randomUUID().toString();
        String requestId = java.util.UUID.randomUUID().toString();
        String extraData = "";

        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        MomoSecurity crypto = new MomoSecurity();
        String signature = crypto.signSHA256(rawHash, secretKey);

        MomoRequest momoMessage = new MomoRequest();
        momoMessage.setPartnerCode(partnerCode);
        momoMessage.setPartnerName("UniDecor");
        momoMessage.setStoreId("UniDecor");
        momoMessage.setRequestId(requestId);
        momoMessage.setAmount(amount);
        momoMessage.setOrderId(orderId);
        momoMessage.setOrderInfo(orderInfo);
        momoMessage.setRedirectUrl(redirectUrl);
        momoMessage.setIpnUrl(ipnUrl);
        momoMessage.setLang("vi");
        momoMessage.setExtraData(extraData);
        momoMessage.setRequestType(requestType);
        momoMessage.setSignature(signature);

        String result = restTemplate.postForObject(endPoint, momoMessage, String.class);
        CustomLogger.info("MOMO Result: "+ result);
        Gson gson = new Gson();
        JsonObject jsonResult = gson.fromJson(result, JsonObject.class);

        String payUrl = jsonResult.get("payUrl").getAsString();
        int resultCode = jsonResult.get("resultCode").getAsInt();
        if(resultCode == 0) {
            return "redirect:"+payUrl;
        } else {
            return "redirect:/cart";
        }
    }
}
