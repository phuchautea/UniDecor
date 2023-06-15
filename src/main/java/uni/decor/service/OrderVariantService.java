package uni.decor.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.entity.*;
import uni.decor.repository.IOrderVariantRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderVariantService {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private IOrderVariantRepository orderVariantRepository;
    public void store(Order order, HttpSession session) {
        List<CartItem> cartItems = cartService.getCartItems(session);
        for (CartItem cartItem : cartItems) {
            String cartItemId = cartItem.getCartItemId();
            Long productId = cartItem.getProductId();
            Product product = productService.getById(cartItem.getProductId());
            ProductVariant productVariant = productVariantService.getById(cartItem.getProductVariantId());
            Long productVariantId = cartItem.getProductVariantId();
            int quantity = cartItem.getQuantity();
            double price = productVariant.getDiscountPrice() > 0 ? productVariant.getDiscountPrice() : productVariant.getPrice();
            OrderVariant orderVariant = new OrderVariant();
            orderVariant.setQuantity(quantity);
            orderVariant.setPrice(price);
            orderVariant.setProductName(product.getName());
            orderVariant.setProductVariantName(productVariant.getName());
            orderVariant.setOrder(order);
            orderVariant.setProduct(product);
            orderVariant.setProduct_variant(productVariant);
            orderVariantRepository.save(orderVariant);
            //update so luong sold_quantity trong productVariant
        }
    }
}
