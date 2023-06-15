package uni.decor.service;

import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.common.CustomLogger;
import uni.decor.dto.CartItemRequest;
import uni.decor.entity.CartItem;
import uni.decor.entity.Product;
import uni.decor.entity.ProductVariant;
import java.util.*;


@Service
public class CartService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductVariantService productVariantService;
    public String generateCartItemId(Long productId, Long productVariantId) {
        return DigestUtils.md5Hex(productId + "_" + productVariantId);
    }

    public boolean add(CartItemRequest request, HttpSession session) {
        int quantity = request.getQuantity();
        Long productId = request.getProductId();
        Long productVariantId = request.getProductVariantId();
        CustomLogger.error("Quantity: "+quantity);
        CustomLogger.error("productId: "+productId);
        CustomLogger.error("productVariantId: "+productVariantId);

        Product product = productService.getById(productId);
        ProductVariant productVariant = productVariantService.getById(productVariantId);

        if(productId == null || productVariantId == null) {
            CustomLogger.error("Thiếu thông tin");
            return false;
        }
        // Kiểm tra productVariantId có phải là của productId không
        if (productVariant == null || productVariant.getProduct().getId() != productId) {
            // Thực hiện xử lý lỗi và trả về false
            CustomLogger.error("Kiểm tra productVariantId có phải là của productId không");
            return false;
        }

        // Kiểm tra số lượng cần mua có hợp lệ hay không
        if (quantity <= 0) {
            CustomLogger.error("Kiểm tra số lượng cần mua có hợp lệ hay không");
            // Thực hiện xử lý lỗi và trả về false
            return false;
        }

        // Kiểm tra số lượng cần mua có đủ không
        if (productVariant.getStock() < quantity) {
            CustomLogger.error("Kiểm tra số lượng cần mua có đủ không");
            // Thực hiện xử lý lỗi và trả về false
            return false;
        }

        // Kiểm tra sản phẩm hợp lệ
        if (productId <= 0 || product == null) {
            CustomLogger.error("Kiểm tra sản phẩm hợp lệ");
            // Thực hiện xử lý lỗi và trả về false
            return false;
        }

        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(generateCartItemId(productId, productVariantId)); // Tạo cartItemId mới
        cartItem.setProductId(productId);
        cartItem.setProductVariantId(productVariantId);
        cartItem.setQuantity(quantity);
        cartItem.setProduct(product);
        cartItem.setProductVariant(productVariant);
        double price = productVariant.getDiscountPrice() > 0 ? productVariant.getDiscountPrice() : productVariant.getPrice();
        double totalPrice = price * quantity;
        cartItem.setTotal(totalPrice);

        // Lưu giỏ hàng vào session
        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");


        if (carts == null) {
            carts = new HashMap<>();
        }
        String cartItemId = generateCartItemId(productId, productVariantId); // Hàm tạo cartItemId từ productId và productVariantId

        if (carts.containsKey(cartItemId)) {
            // Sản phẩm đã tồn tại trong giỏ hàng, cập nhật số lượng
            CartItem existingCartItem = carts.get(cartItemId);
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            // Sản phẩm chưa tồn tại trong giỏ hàng, thêm mới
            carts.put(cartItemId, cartItem);
        }
        // Lấy giỏ hàng của sản phẩm từ giỏ hàng chung

        // Cập nhật giỏ hàng chung
        //carts.put(UUID.randomUUID().toString(), cartItem);

        // Lưu giỏ hàng vào session
        session.setAttribute("carts", carts);
        System.out.println("THÊM THÀNH CÔNG");
        System.out.println(session.getAttribute("carts"));
        // ...
        getCartItems(session);
        return true;
    }
    public boolean update(String cartItemIdUpdated, int quantityUpdated, HttpSession session) {
        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");
        List<CartItem> cartItems = new ArrayList<>();

        if (carts != null) {
            cartItems.addAll(carts.values());

            for (CartItem cartItem : cartItems) {
                String cartItemId = cartItem.getCartItemId();
//                Long productId = cartItem.getProductId();
//                Product product = productService.getById(cartItem.getProductId());
                ProductVariant productVariant = productVariantService.getById(cartItem.getProductVariantId());
//                Long productVariantId = cartItem.getProductVariantId();
//                int quantity = cartItem.getQuantity();
                if(cartItemId.equals(cartItemIdUpdated)) {
                    cartItem.setQuantity(quantityUpdated);
                    double price = productVariant.getDiscountPrice() > 0 ? productVariant.getDiscountPrice() : productVariant.getPrice();
                    double totalPrice = price * quantityUpdated;
                    cartItem.setTotal(totalPrice);
                    session.setAttribute("carts", carts);
                    CustomLogger.error("Cập nhập giỏ hàng thành công!!!");
                    return true;
                }
            }
        }
        return false;
//        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");
//
//        if (carts == null) {
//            CustomLogger.error("Giỏ hàng trống");
//            return false;
//        }
//
//        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
//            String cartItemId = entry.getKey();
//            int quantity = entry.getValue();
//
//            if (quantity <= 0) {
//                CustomLogger.error("Số lượng không hợp lệ");
//                return false;
//            }
//
//            if (carts.containsKey(cartItemId)) {
//                // Cập nhật số lượng của sản phẩm
//                CartItem cartItem = carts.get(cartItemId);
//                cartItem.setQuantity(quantity);
//                double price = cartItem.getProductVariant().getDiscountPrice() > 0 ? cartItem.getProductVariant().getDiscountPrice() : cartItem.getProductVariant().getPrice();
//                double totalPrice = price * quantity;
//                cartItem.setTotal(totalPrice);
//            } else {
//                CustomLogger.error("Sản phẩm không tồn tại trong giỏ hàng");
//                return false;
//            }
//        }
//
//        // Cập nhật giỏ hàng trong session
//        session.setAttribute("carts", carts);
//        CustomLogger.error("Cập nhật thành công");
//        return true;
    }

    public List<CartItem> getCartItems(HttpSession session) {
        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");
        List<CartItem> cartItems = new ArrayList<>();

        if (carts != null) {
            cartItems.addAll(carts.values());

            for (CartItem cartItem : cartItems) {
                String cartItemId = cartItem.getCartItemId();
                Long productId = cartItem.getProductId();
                Product product = productService.getById(cartItem.getProductId());
                ProductVariant productVariant = productVariantService.getById(cartItem.getProductVariantId());
                Long productVariantId = cartItem.getProductVariantId();
                int quantity = cartItem.getQuantity();

                // Thực hiện xử lý với từng thuộc tính của cartItem
                System.out.println("CartItemId: " + cartItemId);
                System.out.println("ProductId: " + productId);
                System.out.println("ProductVariantId: " + productVariantId);
                System.out.println("Quantity: " + quantity);
                // ...
            }
        }

        return cartItems;
    }

    public boolean validateCarts(HttpSession session) {
        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");
        List<CartItem> cartItems = new ArrayList<>();
        int hasProductSoldOut = 0; // flag check product soldout
        if (carts != null) {
            cartItems.addAll(carts.values());

            for (CartItem cartItem : cartItems) {
                String cartItemId = cartItem.getCartItemId();
                Long productId = cartItem.getProductId();
                Product product = productService.getById(cartItem.getProductId());
                ProductVariant productVariant = productVariantService.getById(cartItem.getProductVariantId());
                Long productVariantId = cartItem.getProductVariantId();
                int quantity = cartItem.getQuantity();
                // Kiểm tra số lượng
                if (productVariant.getStock() - cartItem.getQuantity() < 0) {
                    this.remove(cartItem.getCartItemId(), session);
                    hasProductSoldOut = 1;
                    continue;
                }

                // Thực hiện xử lý với từng thuộc tính của cartItem
                System.out.println("CartItemId: " + cartItemId);
                System.out.println("ProductId: " + productId);
                System.out.println("ProductVariantId: " + productVariantId);
                System.out.println("Quantity: " + quantity);
                // ...
            }
            if (hasProductSoldOut == 1) {
                //Session.flash("error", "Đã có sản phẩm không đủ số lượng tồn kho, vui lòng kiểm tra lại");
                return false;
            }
            return true;
        }
        return false;
    }

    public double getTotalPrice(HttpSession session) {
        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");
        double totalPrice = 0;
        if (carts != null) {
            List<CartItem> cartItems = new ArrayList<>(carts.values());
            for (CartItem cartItem : cartItems) {
                String cartItemId = cartItem.getCartItemId();
                // Thực hiện xử lý với từng thuộc tính của cartItem
                System.out.println("CartItemId: " + cartItemId);
                double total = cartItem.getTotal();
                totalPrice += total;
                // ...
            }
        }
        return totalPrice;
    }

    public int countCartItems(HttpSession session) {
        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");
        int count = 0;

        if (carts != null) {
            List<CartItem> cartItems = new ArrayList<>(carts.values());

            for (CartItem cartItem : cartItems) {
                String cartItemId = cartItem.getCartItemId();
                // Thực hiện xử lý với từng thuộc tính của cartItem
                System.out.println("CartItemId: " + cartItemId);
                count++;
                // ...
            }
        }

        return count;
    }

    public boolean remove(String cartItemId, HttpSession session) {
        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");

        if (carts != null) {
            for (Iterator<Map.Entry<String, CartItem>> iterator = carts.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, CartItem> entry = iterator.next();
                String itemId = entry.getKey();
                CartItem cartItem = entry.getValue();

                if (cartItem.getCartItemId().equals(cartItemId)) {
                    iterator.remove();
                    session.setAttribute("carts", carts);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean clear(HttpSession session) {
        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");

        if (carts != null) {
            session.removeAttribute("carts");
            return true;
//            session.setAttribute("carts", "");
//            for (Iterator<Map.Entry<String, CartItem>> iterator = carts.entrySet().iterator(); iterator.hasNext(); ) {
//                Map.Entry<String, CartItem> entry = iterator.next();
//                String itemId = entry.getKey();
//                CartItem cartItem = entry.getValue();
//
//                if (cartItem.getCartItemId().equals(cartItemId)) {
//                    iterator.remove();
//                    session.setAttribute("carts", carts);
//                    return true;
//                }
//            }
        }

        return false;
    }




}
