package uni.decor.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uni.decor.common.CustomLogger;
import uni.decor.dto.CartItemRequest;
import uni.decor.dto.OrderRequest;
import uni.decor.service.OrderService;
import uni.decor.util.EmailValidator;
import uni.decor.util.PhoneNumberValidator;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/order")
    public String store(@ModelAttribute("orderRequest") OrderRequest orderRequest, HttpSession session, RedirectAttributes redirectAttributes) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String name = orderRequest.getBilling_name();
        String billing_address = orderRequest.getBilling_address();
        String province = orderRequest.getProvince_value();
        String district = orderRequest.getDistrict_value();
        String ward = orderRequest.getWard_value();
        String phoneNumber = orderRequest.getBilling_phone();
        String email = orderRequest.getBilling_email();
        String payment = orderRequest.getPayment();
        String address = billing_address + ", " + ward + ", " + district + ", " + province;
        orderRequest.setBilling_address(address);
        List<String> errors = new ArrayList<>();

        if (name.isEmpty()) {
            errors.add("Vui lòng nhập tên");
        }

        if (!PhoneNumberValidator.validateVNPhoneNumber(phoneNumber)) {
            errors.add("Số điện thoại không hợp lệ");
        }

        if (!EmailValidator.validateEmail(email)) {
            errors.add("Email không hợp lệ");
        }

        if (billing_address.isEmpty() || ward.isEmpty() || district.isEmpty() || province.isEmpty()) {
            errors.add("Vui lòng nhập địa chỉ");
        }

        if (!payment.equals("cash") && !payment.equals("momo") && !payment.equals("vnpay")) {
            errors.add("Phương thức thanh toán không hợp lệ");
        }

        if(errors.isEmpty()) {
            return orderService.process(orderRequest, session);
            //return "redirect:/cart";
        } else {
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/cart";
            //  in ra lỗi
        }

//        try {
//            boolean added = orderService.save(orderRequest, session);
//            CustomLogger.error("Thêm order");
//            if (added) {
//                // Redirect to cart page or product detail page
//                return "redirect:/cart"; // Change the URL based on your application
//            } else {
//                // Handle error case, display error message or redirect to an error page
//                return "redirect:/cart"; // Change the view name based on your application
//            }
//        } catch (Exception e) {
//            CustomLogger.error(e.getMessage());
//            return "redirect:/cart"; // Change the URL based on your application
//        }
    }
    @GetMapping("/order/success")
    public String orderSuccess() {
        return "order/success";
    }
}
