package ph.edu.usc.beautyapp2;

import java.util.HashMap;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    private Map<Product, Integer> cartItems;

    private CartManager() {
        cartItems = new HashMap<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product) {
        if (cartItems.containsKey(product)) {
            cartItems.put(product, cartItems.get(product) + 1);
        } else {
            cartItems.put(product, 1);
        }
    }

    public void removeFromCart(Product product) {
        if (cartItems.containsKey(product)) {
            int quantity = cartItems.get(product);
            if (quantity > 1) {
                cartItems.put(product, quantity - 1);
            } else {
                cartItems.remove(product);
            }
        }
    }

    public Map<Product, Integer> getCartItems() {
        return new HashMap<>(cartItems);
    }
}