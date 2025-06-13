package DAO;

import Database.DatabaseC;
import Model.Wisata;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Wisata.
 */
public class WisataDAO {

    private static final Logger LOGGER = Logger.getLogger(WisataDAO.class.getName());

    /**
     * Get all wisata entries sorted descending by id (for newest first).
     */
    public List<Wisata> getAllWisata() {
        List<Wisata> result = new ArrayList<>();

        String sql = "SELECT id, name, category, location, short_desc, full_desc, image_urls FROM wisata ORDER BY id DESC";

        try (Connection conn = DatabaseC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Wisata w = new Wisata();
                w.setId(rs.getInt("id"));
                w.setName(rs.getString("name"));
                w.setCategory(rs.getString("category"));
                w.setLocation(rs.getString("location"));
                w.setShortDescription(rs.getString("short_desc"));
                w.setFullDescription(rs.getString("full_desc"));

                // Assuming image_urls stored as comma separated string
                String imgStr = rs.getString("image_urls");
                List<String> images = (imgStr == null || imgStr.isEmpty())
                        ? new ArrayList<>()
                        : Arrays.asList(imgStr.split(","));
                w.setImageUrls(images);

                result.add(w);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get all wisata", e);
        }

        return result;
    }

    /**
     * Search wisata by filters.
     * All params can be null or empty which means no filtering.
     */
    public List<Wisata> searchWisata(String nameFilter, String categoryFilter, String locationFilter) {
        List<Wisata> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, name, category, location, short_desc, full_desc, image_urls FROM wisata WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (nameFilter != null && !nameFilter.isBlank()) {
            sql.append("AND LOWER(name) LIKE ? ");
            params.add("%" + nameFilter.toLowerCase() + "%");
        }
        if (categoryFilter != null && !categoryFilter.isBlank()) {
            sql.append("AND LOWER(category) = ? ");
            params.add(categoryFilter.toLowerCase());
        }
        if (locationFilter != null && !locationFilter.isBlank()) {
            sql.append("AND LOWER(location) LIKE ? ");
            params.add("%" + locationFilter.toLowerCase() + "%");
        }
        sql.append("ORDER BY id DESC");

        try (Connection conn = DatabaseC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Wisata w = new Wisata();
                    w.setId(rs.getInt("id"));
                    w.setName(rs.getString("name"));
                    w.setCategory(rs.getString("category"));
                    w.setLocation(rs.getString("location"));
                    w.setShortDescription(rs.getString("short_desc"));
                    w.setFullDescription(rs.getString("full_desc"));

                    String imgStr = rs.getString("image_urls");
                    List<String> images = (imgStr == null || imgStr.isEmpty())
                            ? new ArrayList<>()
                            : Arrays.asList(imgStr.split(","));
                    w.setImageUrls(images);

                    result.add(w);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to search wisata", e);
        }
        return result;
    }

    /**
     * Get a Wisata by ID.
     */
    public Wisata getWisataById(int id) {
        Wisata w = null;
        String sql = "SELECT id, name, category, location, short_desc, full_desc, image_urls FROM wisata WHERE id = ?";

        try (Connection conn = DatabaseC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    w = new Wisata();
                    w.setId(rs.getInt("id"));
                    w.setName(rs.getString("name"));
                    w.setCategory(rs.getString("category"));
                    w.setLocation(rs.getString("location"));
                    w.setShortDescription(rs.getString("short_desc"));
                    w.setFullDescription(rs.getString("full_desc"));

                    String imgStr = rs.getString("image_urls");
                    List<String> images = (imgStr == null || imgStr.isEmpty())
                            ? new ArrayList<>()
                            : Arrays.asList(imgStr.split(","));
                    w.setImageUrls(images);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get wisata by id", e);
        }
        return w;
    }

    /**
     * Get all distinct categories for filtering.
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM wisata ORDER BY category";

        try (Connection conn = DatabaseC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get wisata categories", e);
        }
        return categories;
    }
}
