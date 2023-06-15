package uni.decor.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uni.decor.common.CustomLogger;
import uni.decor.config.SecurityUtils;
import uni.decor.dto.CartItemRequest;
import uni.decor.entity.CartItem;
import uni.decor.entity.User;
import uni.decor.service.CartService;
import uni.decor.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {
    private final CartService cartService;
    @Autowired
    private UserService userService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping("/cart")
    public String showAllCart(Model model, HttpSession session){
        CustomLogger.info("Email Session: "+SecurityUtils.getEmailPrincipal()); // Lấy email session
        User userInfo = new User();
        if(SecurityUtils.getEmailPrincipal() != "") {
            userInfo = userService.getUserByEmail(SecurityUtils.getEmailPrincipal());
        }

        List<CartItem> cartItemList = cartService.getCartItems(session);
        int totalCart = cartService.countCartItems(session);
        double getTotalPrice = cartService.getTotalPrice(session);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("totalCart", totalCart);
        model.addAttribute("getTotalPrice", getTotalPrice);
        model.addAttribute("userInfo", userInfo);
        return "cart";
    }
    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("cartItemRequest") CartItemRequest cartItemRequest, HttpSession session) {
        try {
            boolean added = cartService.add(cartItemRequest, session);
            CustomLogger.error("Thêm vào giỏ hàng");
            if (added) {
                // Redirect to cart page or product detail page
                return "redirect:/cart"; // Change the URL based on your application
            } else {
                // Handle error case, display error message or redirect to an error page
                return "redirect:/cart"; // Change the view name based on your application
            }
        } catch (Exception e) {
            CustomLogger.error(e.getMessage());
            return "redirect:/cart"; // Change the URL based on your application
        }
    }

    @GetMapping("/cart/remove/{cartItemId}")
    public String removeFromCart(@PathVariable("cartItemId") String cartItemId, HttpSession session) {
        boolean removed = cartService.remove(cartItemId, session);
        CustomLogger.error("Xoá khỏi giỏ hàng: "+ cartItemId +"");
        if (removed) {
            // Redirect to cart page or product detail page
            return "redirect:/cart"; // Change the URL based on your application
        } else {
            // Handle error case, display error message or redirect to an error page
            return "error"; // Change the view name based on your application
        }
    }

    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        boolean cleared = cartService.clear(session);
        CustomLogger.error("Xoá tất cả sản phẩm ra khỏi giỏ hàng");
        if (cleared) {
            CustomLogger.info("Xoá tất cả giỏ hàng thành công");
            // Redirect to cart page or product detail page
            return "redirect:/cart"; // Change the URL based on your application
        } else {
            CustomLogger.info("Xoá tất cả giỏ hàng thất bại");
            // Handle error case, display error message or redirect to an error page
            return "redirect:/cart"; // Change the view name based on your application
        }
    }

    @PostMapping("/cart/update/{cartItemId}/{quantity}")
    public String updateToCart(@PathVariable("cartItemId") String cartItemId, @PathVariable("quantity") int quantity, HttpSession session) {
        CustomLogger.info("UPDATE CART");
        CustomLogger.info("cartItemId: "+ cartItemId);
        CustomLogger.info("quantity: "+ quantity);
        if(cartService.update(cartItemId, quantity, session)){
            return "cart/true";
            //return "redirect:/cart/true";
        }else{
            return "cart/false";
            //return "redirect:/cart/false";
        }
//        try {
//            Map<String, Integer> quantities = new HashMap<>();
//            quantities.put("ab35e84a215f0f711ed629c2abb9efa0", 2); // cartItemId và quantity tương ứng
//            quantities.put("a1c5285851b5bbc79f967d85ff84f3a4", 3);
//
//            update(quantities, session);
//            boolean updated = cartService.update(cartItemRequest, session);
//            CustomLogger.error("Cập nhật giỏ hàng");
//            if (updated) {
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
}
