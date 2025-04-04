package ph.edu.usc.beautyapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView featuredRecyclerView;
    private RecyclerView popularRecyclerView;
    private ProductAdapter featuredAdapter;
    private ProductAdapter popularAdapter;
    private List<Product> featuredProducts;
    private List<Product> popularProducts;
    private List<Product> allFeaturedProducts;
    private List<Product> allPopularProducts;
    private List<Product> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        ImageView cart_icon = findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        featuredRecyclerView = findViewById(R.id.featured_products_list);
        popularRecyclerView = findViewById(R.id.popular_products_list);
        Spinner sortSpinnerPopular = findViewById(R.id.sort_spinner_popular);
        Spinner sortSpinnerFeatured = findViewById(R.id.sort_spinner_featured);
        Spinner filterSpinnerPopular = findViewById(R.id.filter_spinner_popular);
        Spinner filterSpinnerFeatured = findViewById(R.id.filter_spinner_featured);
        Spinner categorySpinnerPopular = findViewById(R.id.category_spinner_popular);
        Spinner categorySpinnerFeatured = findViewById(R.id.category_spinner_featured);

        featuredProducts = new ArrayList<>();
        popularProducts = new ArrayList<>();
        allFeaturedProducts = new ArrayList<>();
        allPopularProducts = new ArrayList<>();

        // Save sample data to JSON (only needed once, comment out after first run)
        // saveSampleDataToJSON();

        // Load data from JSON
        loadDataFromJSON();

        // Combine all products
        allProducts = new ArrayList<>();
        allProducts.addAll(allFeaturedProducts);
        allProducts.addAll(allPopularProducts);

        // Set all products in CartManager
        CartManager.getInstance().setAllProducts(allProducts);

        // Load the cart from JSON
        CartManager.getInstance().loadCart(this);

        featuredAdapter = new ProductAdapter(featuredProducts, this, this::onProductClick);
        popularAdapter = new ProductAdapter(popularProducts, this, this::onProductClick);

        // Set layout managers to horizontal
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredRecyclerView.setAdapter(featuredAdapter);

        popularRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        popularRecyclerView.setAdapter(popularAdapter);

        // Set up the sort spinner for popular products
        ArrayAdapter<CharSequence> adapterPopular = ArrayAdapter.createFromResource(this, R.array.sort_options, android.R.layout.simple_spinner_item);
        adapterPopular.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinnerPopular.setAdapter(adapterPopular);

        sortSpinnerPopular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Price: Low to High
                        Collections.sort(popularProducts, Comparator.comparingDouble(Product::getPrice));
                        break;
                    case 1: // Price: High to Low
                        Collections.sort(popularProducts, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                        break;
                    case 2: // Rating: High to Low
                        Collections.sort(popularProducts, (p1, p2) -> Double.compare(p2.getRating(), p1.getRating()));
                        break;
                }
                popularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up the sort spinner for featured products
        ArrayAdapter<CharSequence> adapterFeatured = ArrayAdapter.createFromResource(this, R.array.sort_options, android.R.layout.simple_spinner_item);
        adapterFeatured.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinnerFeatured.setAdapter(adapterFeatured);

        sortSpinnerFeatured.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Price: Low to High
                        Collections.sort(featuredProducts, Comparator.comparingDouble(Product::getPrice));
                        break;
                    case 1: // Price: High to Low
                        Collections.sort(featuredProducts, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                        break;
                    case 2: // Rating: High to Low
                        Collections.sort(featuredProducts, (p1, p2) -> Double.compare(p2.getRating(), p1.getRating()));
                        break;
                }
                featuredAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up the filter spinner for popular products
        ArrayAdapter<CharSequence> adapterFilterPopular = ArrayAdapter.createFromResource(this, R.array.filter_options, android.R.layout.simple_spinner_item);
        adapterFilterPopular.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinnerPopular.setAdapter(adapterFilterPopular);

        filterSpinnerPopular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                filterPopularProducts(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up the filter spinner for featured products
        ArrayAdapter<CharSequence> adapterFilterFeatured = ArrayAdapter.createFromResource(this, R.array.filter_options, android.R.layout.simple_spinner_item);
        adapterFilterFeatured.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinnerFeatured.setAdapter(adapterFilterFeatured);

        filterSpinnerFeatured.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                filterFeaturedProducts(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up the category spinner for popular products
        ArrayAdapter<CharSequence> adapterCategoryPopular = ArrayAdapter.createFromResource(this, R.array.category_options, android.R.layout.simple_spinner_item);
        adapterCategoryPopular.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerPopular.setAdapter(adapterCategoryPopular);

        categorySpinnerPopular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterPopularByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up the category spinner for featured products
        ArrayAdapter<CharSequence> adapterCategoryFeatured = ArrayAdapter.createFromResource(this, R.array.category_options, android.R.layout.simple_spinner_item);
        adapterCategoryFeatured.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerFeatured.setAdapter(adapterCategoryFeatured);

        categorySpinnerFeatured.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterFeaturedByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void saveSampleDataToJSON() {
        try {
            JSONArray jsonArray = new JSONArray();

            // Add featured products
            for (Product product : allFeaturedProducts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", product.getName());
                jsonObject.put("description", product.getDescription());
                jsonObject.put("price", product.getPrice());
                jsonObject.put("rating", product.getRating());
                jsonObject.put("skinType", product.getSkinType());
                jsonObject.put("imageResId", product.getImageResId());
                jsonObject.put("category", product.getCategory());
                jsonArray.put(jsonObject);
            }

            // Add popular products
            for (Product product : allPopularProducts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", product.getName());
                jsonObject.put("description", product.getDescription());
                jsonObject.put("price", product.getPrice());
                jsonObject.put("rating", product.getRating());
                jsonObject.put("skinType", product.getSkinType());
                jsonObject.put("imageResId", product.getImageResId());
                jsonObject.put("category", product.getCategory());
                jsonArray.put(jsonObject);
            }

            String jsonString = jsonArray.toString();
            FileOutputStream fos = openFileOutput("products.json", MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            fos.close();

            Toast.makeText(this, "Sample data saved to JSON", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving sample data to JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDataFromJSON() {

        // featured products

        try {
            InputStream is = getResources().openRawResource(R.raw.featprod);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(jsonString.toString());
            allFeaturedProducts.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                double price = jsonObject.getDouble("price");
                double rating = jsonObject.getDouble("rating");
                String skinType = jsonObject.getString("skinType");
                int imageResId = getResources().getIdentifier(jsonObject.getString("imageResId"), "drawable", getPackageName());
                String category = jsonObject.getString("category");

                Product product = new Product(name, description, price, rating, skinType, imageResId, category);

                allFeaturedProducts.add(product);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading data from JSON", Toast.LENGTH_SHORT).show();
        }

        // popular products

        try {
            InputStream is = getResources().openRawResource(R.raw.popprod);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(jsonString.toString());
            allPopularProducts.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                double price = jsonObject.getDouble("price");
                double rating = jsonObject.getDouble("rating");
                String skinType = jsonObject.getString("skinType");
                int imageResId = getResources().getIdentifier(jsonObject.getString("imageResId"), "drawable", getPackageName());
                String category = jsonObject.getString("category");

                Product product = new Product(name, description, price, rating, skinType, imageResId, category);

                allPopularProducts.add(product);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading data from JSON", Toast.LENGTH_SHORT).show();
        }

    }

    private void filterPopularProducts(String filter) {
        if (filter.equals("All")) {
            popularProducts.clear();
            popularProducts.addAll(allPopularProducts);
        } else {
            popularProducts.clear();
            popularProducts.addAll(allPopularProducts.stream()
                    .filter(product -> product.getSkinType().equals(filter))
                    .collect(Collectors.toList()));
        }
        popularAdapter.notifyDataSetChanged();
    }

    private void filterFeaturedProducts(String filter) {
        if (filter.equals("All")) {
            featuredProducts.clear();
            featuredProducts.addAll(allFeaturedProducts);
        } else {
            featuredProducts.clear();
            featuredProducts.addAll(allFeaturedProducts.stream()
                    .filter(product -> product.getSkinType().equals(filter))
                    .collect(Collectors.toList()));
        }
        featuredAdapter.notifyDataSetChanged();
    }

    private void filterPopularByCategory(String category) {
        if (category.equals("All")) {
            popularProducts.clear();
            popularProducts.addAll(allPopularProducts);
        } else {
            popularProducts.clear();
            popularProducts.addAll(allPopularProducts.stream()
                    .filter(product -> product.getCategory().equals(category))
                    .collect(Collectors.toList()));
        }
        popularAdapter.notifyDataSetChanged();
    }

    private void filterFeaturedByCategory(String category) {
        if (category.equals("All")) {
            featuredProducts.clear();
            featuredProducts.addAll(allFeaturedProducts);
        } else {
            featuredProducts.clear();
            featuredProducts.addAll(allFeaturedProducts.stream()
                    .filter(product -> product.getCategory().equals(category))
                    .collect(Collectors.toList()));
        }
        featuredAdapter.notifyDataSetChanged();
    }

    private void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }
}