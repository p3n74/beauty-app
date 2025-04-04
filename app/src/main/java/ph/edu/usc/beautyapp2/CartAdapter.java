package ph.edu.usc.beautyapp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> cartItems;
    private CartActivity cartActivity;

    public CartAdapter(List<Product> cartItems, CartActivity cartActivity) {
        this.cartItems = cartItems;
        this.cartActivity = cartActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());
        holder.productImage.setImageResource(product.getImageResId());

        holder.removeButton.setOnClickListener(v -> {
            // Remove from CartManager
            CartManager.getInstance().removeFromCart(product);

            // Remove from local list
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());

            // Update total
            cartActivity.updateTotal();

            // Save updated cart to JSON
            cartActivity.saveCartToJSON();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;
        Button removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
}