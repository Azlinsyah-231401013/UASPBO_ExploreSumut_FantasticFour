package Model;

import java.time.LocalDateTime;

/**
 * Model class for a review/ulasan.
 */
public class Review {
    private int id;
    private int wisataId;
    private String userName;
    private int rating; // 1-5 stars
    private String comment;
    private LocalDateTime reviewTime;

    public Review() {}

    public Review(int id, int wisataId, String userName, int rating, String comment, LocalDateTime reviewTime) {
        this.id = id;
        this.wisataId = wisataId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.reviewTime = reviewTime;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getWisataId() { return wisataId; }
    public void setWisataId(int wisataId) { this.wisataId = wisataId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getReviewTime() { return reviewTime; }
    public void setReviewTime(LocalDateTime reviewTime) { this.reviewTime = reviewTime; }

    @Override
    public String toString() {
        return String.format("%s: %d stars - %s", userName, rating, comment);
    }
}
