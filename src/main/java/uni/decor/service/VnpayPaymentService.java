package uni.decor.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uni.decor.common.CustomLogger;
import uni.decor.encryption.VNPaySecurity;
import uni.decor.entity.Order;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static uni.decor.encryption.VNPaySecurity.getRandomNumber;

@Service
public class VnpayPaymentService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${uniDecor.vnpay.vnp_TmnCode}")
    private String tmnCode;
    @Value("${uniDecor.vnpay.vnp_BankCode}")
    private String bankCode;
    @Value("${uniDecor.vnpay.vnp_ReturnUrl}")
    private String returnUrl;
    @Value("${uniDecor.vnpay.vnp_Url}")
    private String url;
    @Value("${uniDecor.vnpay.vnp_HashSecret}")
    private String hashSecret;
    @Value("${uniDecor.vnpay.vnp_PayUrl}")
    private String PayUrl;

    public String process(int total_price, HttpSession session) throws UnsupportedEncodingException {
        Order order = (Order) session.getAttribute("order");
        String amount = String.valueOf(total_price * 100);

        String vnp_TxnRef = getRandomNumber(8); // này là mã đơn hàng
        String vnp_TmnCode = tmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", tmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        CustomLogger.info("vnp_Params: "+vnp_Params);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPaySecurity.hmacSHA512(hashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String payUrl = PayUrl + "?" + queryUrl;
        CustomLogger.info(payUrl);
        return "redirect:"+payUrl;

    }
}
