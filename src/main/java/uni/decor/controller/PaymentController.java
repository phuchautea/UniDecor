package uni.decor.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uni.decor.entity.Order;
import uni.decor.service.OrderService;

@Controller
public class PaymentController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/pay/momoResult")
    public String momoResult(@RequestParam("resultCode") String resultCode, HttpSession session) {
        // Kiểm tra thêm từ WebHook như vậy ko an toàn
        if (resultCode.equals("0")) {
            Order order = (Order) session.getAttribute("order");
            order.setPaymentStatus("paid");
            // Thêm Order States "ĐÃ THANH TOÁN"
            return orderService.store(order, session);
        }
        return "redirect:/pay/error";
    }
    @GetMapping("/pay/error")
    public String error() {
        return "pay/error";
    }
//    public function momoResult(Request $request){
//
//        $resultCode = $request->get('resultCode');
//        if ($resultCode == '0')
//        {
//
//            $carts = Session::get('carts');
//            $order = Session::get('order');
//            $order['payment_status'] = 'paid';
//            $order['status'] = '1';
//            $this->orderService->store($order);
////            $order_id = $this->orderService->add($order); // Thêm vào order
////            if ($order_id != 0) {
////                $order_details = [];
////                $order_details['carts'] = $carts;
////                $order_details['order_id'] = $order_id;
////                $this->orderDetailService->add($order_details);
////                $this->cartService->remove(0); // xóa hết giỏ hàng
//////                Session::pull('customer');
////                Session::pull('order');
////            }
//            return redirect('/order/success');
//            // thanh toán thành công, đính kèm mã order để tra cứu, bằng session::flash
//        }
//        return redirect('/pay/error'); // thanh toán thất bại
//    }
    @GetMapping("/pay/vnpayResult")
    public String vnpayResult(@RequestParam("vnp_ResponseCode") String vnp_ResponseCode, HttpSession session) {
        if (vnp_ResponseCode.equals("00")) {
            Order order = (Order) session.getAttribute("order");
            order.setPaymentStatus("paid");
            // Thêm Order States "ĐÃ THANH TOÁN"
            return orderService.store(order, session);
        }
        return "redirect:/pay/error";
    }
}
