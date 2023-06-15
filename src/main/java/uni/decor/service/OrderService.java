package uni.decor.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uni.decor.common.CustomLogger;
import uni.decor.config.SecurityUtils;
import uni.decor.dto.OrderRequest;
import uni.decor.entity.*;
import uni.decor.repository.IOrderRepository;
import uni.decor.util.NumberUtils;
import uni.decor.util.RandomStringGenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class OrderService {
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private MomoPaymentService momoPaymentService;
    @Autowired
    private VnpayPaymentService vnpayPaymentService;
    @Autowired
    private OrderVariantService orderVariantService;
    @Autowired
    private ProductVariantService productVariantService;
    public String process(OrderRequest request, HttpSession session) throws NoSuchAlgorithmException, InvalidKeyException {
        String billing_name = request.getBilling_name();
        String billing_address = request.getBilling_address();
        String province_value = request.getProvince_value();
        String district_value = request.getDistrict_value();
        String ward_value = request.getWard_value();
        String billing_phone = request.getBilling_phone();
        String billing_email = request.getBilling_email();
        String payment = request.getPayment();
        User userInfo = new User();
        if(SecurityUtils.getEmailPrincipal() != "") {
            userInfo = userService.getUserByEmail(SecurityUtils.getEmailPrincipal());
        }
//        Long userId = userInfo.getId();
//        CustomLogger.info("userId:" + userId);
        if(cartService.validateCarts(session)) {

            // Thêm order vào session
            Order orderInfo = new Order();
            orderInfo.setReceiverName(billing_name);
            orderInfo.setReceiverAddress(billing_address);
            orderInfo.setReceiverPhone(billing_phone);
            orderInfo.setReceiverEmail(billing_email);
            orderInfo.setPaymentMethod(payment);
            orderInfo.setPaymentStatus("unpaid");
            orderInfo.setTotalPrice(cartService.getTotalPrice(session));
            orderInfo.setUser(userInfo);
            session.setAttribute("order", orderInfo);

            double total_price = cartService.getTotalPrice(session);
            // Thêm phương thức thanh toán vào session['payment'] và chuyển tới phương thức thanh toán
            switch (payment) {
                case "momo":
                    return momoPaymentService.process((int)total_price, session);
                case "vnpay":
                    return vnpayPaymentService.process((int)total_price, session);
                default:
                    // Thêm order states ở đây ("đặt hàng");
                    // Add order, trạng thái chưa thanh toán, xóa session cart
                    return store((Order) session.getAttribute("order"), session);
//                    order.put("status", "0");
//                    return self.store(order);
            }

//            Session.flash("success", "Tạo đơn hàng thành công");
//            Log.info("Order thành công");
            //return "redirect:/cart/true";
        }else{
            return "redirect:/cart/false";
        }
//        try {
//            // Lấy tổng tiền từ giỏ hàng
//            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> carts = Session.get("carts");
//            int total_price = 0;
//            int hasProductSoldOut = 0; // flag check product soldout
//
//            // Kiểm tra giỏ hàng
//            if (!carts.isEmpty()) {
//                for (Map.Entry<Integer, HashMap<Integer, HashMap<String, Integer>>> entry : carts.entrySet()) {
//                    int product_id = entry.getKey();
//                    HashMap<Integer, HashMap<String, Integer>> variants = entry.getValue();
//
//                    for (Map.Entry<Integer, HashMap<String, Integer>> variantEntry : variants.entrySet()) {
//                        int variant_id = variantEntry.getKey();
//                        HashMap<String, Integer> details = variantEntry.getValue();
//
//                        ProductVariant productVariant = productVariantService.getById(variant_id);
//
//                        // Kiểm tra số lượng
//                        if (productVariant.getQuantity() - details.get("quantity") < 0) {
//                            cartService.remove(details.get("cartItemId"));
//                            hasProductSoldOut = 1;
//                            continue;
//                        }
//
//                        // Tính tổng tiền
//                        int price = 0;
//                        if (productVariant.getDiscountPrice() > 0) {
//                            String[] priceArray = String.valueOf(productVariant.getDiscountPrice()).split("\\.");
//                            price = Integer.parseInt(priceArray[0]);
//                        } else {
//                            String[] priceArray = String.valueOf(productVariant.getPrice()).split("\\.");
//                            price = Integer.parseInt(priceArray[0]);
//                        }
//                        total_price += price * details.get("quantity");
//                    }
//                }
//            }
//
//            if (hasProductSoldOut == 1) {
//                Session.flash("error", "Đã có sản phẩm không đủ số lượng tồn kho, vui lòng kiểm tra lại");
//                return redirect("/carts");
//            }
//
//            // Lưu thông tin order lên session
//            HashMap<String, Object> order = Session.get("order"); // lưu thông tin về order
//            order.put("note", note);
//            order.put("payment_method", payment);
//            order.put("payment_status", "unpaid");
//            order.put("total_price", total_price);
//            order.put("user_id", user_id);
//            order.put("name", name);
//            order.put("phoneNumber", phoneNumber);
//            order.put("address", address);
//            order.put("email", email);
//            Session.put("order", order);
//
//            // Thêm phương thức thanh toán vào session['payment'] và chuyển tới phương thức thanh toán
//            switch (payment) {
//                case "momo":
//                    return momoPaymentService.process(total_price);
//                case "vnpay":
//                    return vnpayPaymentService.process(total_price);
//                default:
//                    // Add order, trạng thái chưa thanh toán, xóa session cart
//                    order.put("status", "0");
//                    return self.store(order);
//            }
//
//            Session.flash("success", "Tạo đơn hàng thành công");
//            Log.info("Order thành công");
//            return true;
//        } catch (Exception ex) {
//            Session.flash("error", ex.getMessage());
//            Log.info(ex.getMessage());
//            return false;
//        }
//        return true;
    }
    public String store(Order orderInfo, HttpSession session)
    {
        orderInfo.setCode(RandomStringGenerator.generateRandomString(10));
        Order newOrder = orderRepository.save(orderInfo);
        if (newOrder.getId() != null) {
            session.setAttribute("orderCode", newOrder.getCode()); // Đính kèm mã order để tra cứu đơn hàng
            orderVariantService.store(newOrder, session);

            // Gửi mail
            sendConfirmationEmail(newOrder, session);
            cartService.clear(session);
            session.removeAttribute("order");

            return "redirect:/order/success";
        } else {
            if (newOrder.getPaymentMethod().equals("cash")) {
                return "redirect:/order/error";
            }else{
                return "redirect:/pay/error";
            }
        }
//        $carts = Session::get("carts");
//        $newOrder = $this->orderRepository->store($order); // Thêm vào order
//
//        if ($newOrder) {
//            Session::flash('orderCode', $newOrder->code); // Đính kèm mã order để tra cứu đơn hàng
//            $order_details = [];
//            $order_details['carts'] = $carts;
//            $order_details['order_id'] = $newOrder->id;
//            $this->orderDetailService->store($order_details);
//            $this->cartService->remove(0); // xóa hết giỏ hàng
//            Session::pull('order');
//            Mail::to($newOrder->email)->send(new OrderSuccessMail($newOrder, $this->orderDetailService));
//
//            return redirect('/order/success');
//        }else{
//            return redirect('/carts');
//        }

    }
    public String readFile(String filePath) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    private void sendConfirmationEmail(Order order, HttpSession session) {
        String receiverEmail = order.getReceiverEmail();
        String receiverName = order.getReceiverName();
        String receiverPhone = order.getReceiverPhone();
        String receiverAddress = order.getReceiverAddress();
        String code = order.getCode();
        StringBuilder strSanPham = new StringBuilder();
        double thanhtien = 0.0;
        double tongTien = 0.0;
        List<CartItem> cartItems = cartService.getCartItems(session);
        for (CartItem cartItem : cartItems) {
            ProductVariant variant = productVariantService.getById(cartItem.getProductVariantId());
            double price = variant.getDiscountPrice() > 0 ? variant.getDiscountPrice() : variant.getPrice();
            thanhtien = price * cartItem.getQuantity();
            tongTien += thanhtien;
            strSanPham.append("<tr>");
            strSanPham.append("<td>").append(variant.getName()).append("</td>");
            strSanPham.append("<td>").append(cartItem.getQuantity()).append("</td>");
            strSanPham.append("<td>").append(NumberUtils.formatNumber(thanhtien)).append("</td>");
            strSanPham.append("</tr>");
        }
        CustomLogger.info("strSanPham: "+ strSanPham);


        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(receiverEmail);
            helper.setSubject("Xác nhận đơn hàng");

            String htmlTemplate = readFile("/templates/order/confirmation_template.html");
            htmlTemplate = htmlTemplate.replace("${customerName}", receiverName);
            htmlTemplate = htmlTemplate.replace("${customerPhone}", receiverPhone);
            htmlTemplate = htmlTemplate.replace("${customerEmail}", receiverEmail);
            htmlTemplate = htmlTemplate.replace("${customerAddress}", receiverAddress);
            htmlTemplate = htmlTemplate.replace("${code}", code);
            htmlTemplate = htmlTemplate.replace("${orderVariants}", strSanPham);
            htmlTemplate = htmlTemplate.replace("${tongTien}", NumberUtils.formatNumber(tongTien));
            helper.setText(htmlTemplate, true);

            // Gửi email
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi khi gửi email
        }
    }
    public Order getByCode(String code) {
        return orderRepository.findByCode(code);
    }
}


