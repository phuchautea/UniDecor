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
//        $endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
//        $partnerCode = env('MOMO_partnerCode');
//        $accessKey = env('MOMO_accessKey');
//        $secretKey = env('MOMO_secretKey');
//        $orderInfo = "Thanh toán qua MoMo";
//        $orderId = time() . "";
//        $redirectUrl = env('MOMO_redirectUrl');
//        $ipnUrl = env('MOMO_ipnUrl');
//        $extraData = "";
//
//        $requestId = time() . "";
//        $requestType = "captureWallet";
//        $extraData = "";
//        //before sign HMAC SHA256 signature
//        $rawHash = "accessKey=" . $accessKey . "&amount=" . $amount . "&extraData=" . $extraData . "&ipnUrl=" . $ipnUrl . "&orderId=" . $orderId . "&orderInfo=" . $orderInfo . "&partnerCode=" . $partnerCode . "&redirectUrl=" . $redirectUrl . "&requestId=" . $requestId . "&requestType=" . $requestType;
//        $signature = hash_hmac("sha256", $rawHash, $secretKey);
//        $data = array(
//                'partnerCode' => $partnerCode,
//                'partnerName' => "Test",
//                'storeId' => "MomoTestStore",
//                'requestId' => $requestId,
//                'amount' => $amount,
//                'orderId' => $orderId,
//                'orderInfo' => $orderInfo,
//                'redirectUrl' => $redirectUrl,
//                'ipnUrl' => $ipnUrl,
//                'lang' => 'vi',
//                'extraData' => $extraData,
//                'requestType' => $requestType,
//                'signature' => $signature
//        );
//        String endPoint = env.getProperty("payment.momo.endpoint");
//        String partnerCode = env.getProperty("payment.momo.partner-code");
//        String accessKey = env.getProperty("payment.momo.access-key");
//        String secretKey = env.getProperty("payment.momo.secret-key");
        String requestType = "captureWallet";
        String orderInfo = "Thanh toán đơn hàng";
        CustomLogger.info("Amount: "+total_price);
        String amount = String.valueOf(total_price);
        //String amount1 = String.valueOf(total_price).replaceAll("[^\\d]", ""); // Xóa dấu phẩy
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
        // Sign signature SHA256
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
