package Model;

import java.util.List;

/**
 * Model class for tourist destination (Wisata).
 */
public class Wisata {
    private int id;
    private String name;
    private String category;
    private String location;
    private String shortDescription;
    private String fullDescription;
    private List<String> imageUrls; // Image URLS for gallery

    public Wisata() {}

    public Wisata(int id, String name, String category, String location,
                  String shortDescription, String fullDescription, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.location = location;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.imageUrls = imageUrls;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getFullDescription() { return fullDescription; }
    public void setFullDescription(String fullDescription) { this.fullDescription = fullDescription; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, category, location);
    }
}

