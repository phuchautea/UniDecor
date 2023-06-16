package uni.decor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uni.decor.common.Enum;
import uni.decor.entity.Category;
import uni.decor.entity.Order;
import uni.decor.service.OrderService;
import uni.decor.service.OrderVariantService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class ManageOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderVariantService orderVariantService;

    @GetMapping
    public String showAllOrders(Model model) {
        List<Order> orders = orderService.getAll();
        List<Enum.PaymentStatus> paymentStatusList = Arrays.asList(Enum.PaymentStatus.values());
        List<Enum.OrderStatus> orderStatusList = Arrays.asList(Enum.OrderStatus.values());

        model.addAttribute("payment_status_list", paymentStatusList);
        model.addAttribute("status_list", orderStatusList);
        model.addAttribute("orders", orders);
        return "admin/order/list";
    }


}
