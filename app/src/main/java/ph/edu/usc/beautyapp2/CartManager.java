package ph.edu.usc.beautyapp2;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    private Map<Product, Integer> cartItems;
    private List<Product> allProducts; // List of all available products

    private CartManager() {
        cartItems = new HashMap<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void setAllProducts(List<Product> products) {
        this.allProducts = products;
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

    public void loadCart(Context context) {
        try {
            FileInputStream fis = context.openFileInput("cart.json");
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            String jsonString = new String(inputBuffer);
            isr.close();

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                double price = jsonObject.getDouble("price");

                Product product = findProductByNameAndPrice(name, price);
                if (product != null) {
                    addToCart(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Product findProductByNameAndPrice(String name, double price) {
        if (allProducts != null) {
            for (Product product : allProducts) {
                if (product.getName().equals(name) && product.getPrice() == price) {
                    return product;
                }
            }
        }
        return null;
    }
}