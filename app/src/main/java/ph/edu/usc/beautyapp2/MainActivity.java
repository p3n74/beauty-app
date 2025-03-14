package ph.edu.usc.beautyapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Navbar



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView profileIcon = findViewById(R.id.profile_icon);

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageView cart_icon = findViewById(R.id.cart_icon);

        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        featuredRecyclerView = findViewById(R.id.featured_products_list);
        popularRecyclerView = findViewById(R.id.popular_products_list);
        Spinner sortSpinnerPopular = findViewById(R.id.sort_spinner_popular);
        Spinner sortSpinnerFeatured = findViewById(R.id.sort_spinner_featured);
        Spinner filterSpinnerPopular = findViewById(R.id.filter_spinner_popular);
        Spinner filterSpinnerFeatured = findViewById(R.id.filter_spinner_featured);
        Spinner categorySpinnerPopular = findViewById(R.id.category_spinner_popular);
        Spinner categorySpinnerFeatured = findViewById(R.id.category_spinner_featured);

        // Sample data for featured products
        allFeaturedProducts = new ArrayList<>();
        allFeaturedProducts.add(new Product("Featured Product 1", "Description 1", 29.99, 4.5, "Normal", R.drawable.ic_product1, "Ranked"));
        allFeaturedProducts.add(new Product("Featured Product 2", "Description 2", 19.99, 4.0, "Dry", R.drawable.ic_product2, "Hot"));
        allFeaturedProducts.add(new Product("Featured Product 3", "Description 3", 34.99, 4.7, "Oily", R.drawable.ic_product3, "Loved"));
        allFeaturedProducts.add(new Product("Featured Product 4", "Description 4", 22.99, 4.3, "Combination", R.drawable.ic_product4, "Secret"));
        allFeaturedProducts.add(new Product("Featured Product 5", "Description 5", 27.99, 4.6, "Sensitive", R.drawable.ic_product5, "Ranked"));

        // Sample data for popular products
        allPopularProducts = new ArrayList<>();
        allPopularProducts.add(new Product("Popular Product 1", "Description 6", 39.99, 4.8, "Oily", R.drawable.ic_product6, "Hot"));
        allPopularProducts.add(new Product("Popular Product 2", "Description 7", 24.99, 4.2, "Combination", R.drawable.ic_product7, "Loved"));
        allPopularProducts.add(new Product("Popular Product 3", "Description 8", 31.99, 4.9, "Normal", R.drawable.ic_product8, "Secret"));
        allPopularProducts.add(new Product("Popular Product 4", "Description 9", 26.99, 4.1, "Dry", R.drawable.ic_product9, "Ranked"));
        allPopularProducts.add(new Product("Popular Product 5", "Description 10", 29.49, 4.4, "Sensitive", R.drawable.ic_product10, "Hot"));

        featuredProducts = new ArrayList<>(allFeaturedProducts);
        popularProducts = new ArrayList<>(allPopularProducts);

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