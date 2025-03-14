package ph.edu.usc.beautyapp2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage, homeIcon;
    private TextView productName, productDescription, productPrice, productRating;
    private Button addToCartButton;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        homeIcon = findViewById(R.id.home_icon);
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        productRating = findViewById(R.id.product_rating);
        addToCartButton = findViewById(R.id.add_to_cart_button);

        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText("$" + product.getPrice());
            productRating.setText(product.getRating() + " ★");
            productImage.setImageResource(product.getImageResId());
        }

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartManager.getInstance().addToCart(product);
                int quantity = CartManager.getInstance().getCartItems().get(product);
                Toast.makeText(ProductDetailsActivity.this, "Added to cart. Quantity: " + quantity, Toast.LENGTH_SHORT).show();
            }
        });

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to MainActivity
            }
        });
    }
}