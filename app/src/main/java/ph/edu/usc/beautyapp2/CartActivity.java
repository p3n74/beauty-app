package ph.edu.usc.beautyapp2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartItems;
    private TextView totalText;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        totalText = findViewById(R.id.total_text);
        checkoutButton = findViewById(R.id.checkout_button);
        cartItems = new ArrayList<>();

        ImageView home_icon = findViewById(R.id.home_icon);

        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView profileIcon = findViewById(R.id.profile_icon);

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        // Populate cartItems with individual products
        for (Product product : CartManager.getInstance().getCartItems().keySet()) {
            int quantity = CartManager.getInstance().getCartItems().get(product);
            for (int i = 0; i < quantity; i++) {
                cartItems.add(product);
            }
        }

        cartAdapter = new CartAdapter(cartItems, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        updateTotal();

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckoutDialog();
            }
        });
    }

    void updateTotal() {
        double total = 0.0;
        for (Product product : cartItems) {
            total += product.getPrice();
        }
        totalText.setText("Total: $" + String.format("%.2f", total));

    }

    private void showCheckoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thank you for your purchase");
        builder.setMessage("We've received your order and it will ship in 5-7 business days.\nYour order number is #12345.");
        builder.setPositiveButton("Back to Home", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // Close the CartActivity
            }
        });
        builder.setIcon(R.drawable.ic_check); // Use a check icon if available
        builder.show();
    }
}