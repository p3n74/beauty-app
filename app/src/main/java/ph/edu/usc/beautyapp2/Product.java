package ph.edu.usc.beautyapp2;


import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private String description;
    private double price;
    private double rating;
    private String skinType;
    private int imageResId;
    private String category; // New field for category

    public Product(String name, String description, double price, double rating, String skinType, int imageResId, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.skinType = skinType;
        this.imageResId = imageResId;
        this.category = category;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public double getRating() { return rating; }
    public String getSkinType() { return skinType; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }
}