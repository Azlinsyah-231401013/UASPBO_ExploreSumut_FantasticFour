package com.example.tes_labpbo;

import DAO.ReviewDAO;
import DAO.WisataDAO;
import Model.Review;
import Model.Wisata;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

/**
 * Controller for DetailView.fxml.
 * Shows full description, image gallery, reviews, and review submission.
 */
public class DetailController {

    @FXML private Label nameLabel;
    @FXML private Label categoryLabel;
    @FXML private Label locationLabel;
    @FXML private TextArea fullDescriptionArea;
    @FXML private HBox imageGallery;

    @FXML private ListView<Review> reviewListView;
    @FXML private TextField nameField;
    @FXML private ComboBox<Integer> ratingComboBox;
    @FXML private TextArea commentArea;

    private int wisataId;
    private WisataDAO wisataDAO = new WisataDAO();
    private ReviewDAO reviewDAO = new ReviewDAO();

    @FXML
    private void initialize() {
        // Setup rating ComboBox 1-5
        ratingComboBox.getItems().addAll(1, 2, 3, 4, 5);
    }

    /**
     * Set Wisata Id and load detail data.
     */
    public void setWisataId(int id) {
        this.wisataId = id;
        loadWisataDetail();
        loadReviews();
    }

    private void loadWisataDetail() {
        Wisata wisata = wisataDAO.getWisataById(wisataId);
        if (wisata == null) return;

        Platform.runLater(() -> {
            nameLabel.setText(wisata.getName());
            categoryLabel.setText("Kategori: " + wisata.getCategory());
            locationLabel.setText("Lokasi: " + wisata.getLocation());
            fullDescriptionArea.setText(wisata.getFullDescription());

            imageGallery.getChildren().clear();
            if (wisata.getImageUrls() != null) {
                for (String url : wisata.getImageUrls()) {
                    if (url != null && !url.isBlank()) {
                        ImageView imageView = new ImageView(new Image(url.trim(), 150, 100, true, true));
                        imageView.setStyle("-fx-effect: dropshadow(gaussian, #999, 3, 0.3, 1, 1);");
                        imageGallery.getChildren().add(imageView);
                    }
                }
            }
        });
    }

    private void loadReviews() {
        List<Review> reviews = reviewDAO.getReviewsByWisataId(wisataId);
        Platform.runLater(() -> {
            reviewListView.getItems().clear();
            reviewListView.getItems().addAll(reviews);
            reviewListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Review item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        String stars = "★".repeat(item.getRating()) + "☆".repeat(5 - item.getRating());
                        String text = String.format("%s\n%s\n%s", item.getUserName(), stars, item.getComment());
                        setText(text);
                    }
                }
            });
        });
    }

    @FXML
    private void onSubmitReview() {
        String userName = nameField.getText();
        Integer rating = ratingComboBox.getValue();
        String comment = commentArea.getText();

        if (userName == null || userName.isBlank()) {
            showAlert("Validasi", "Nama harus diisi.");
            return;
        }
        if (rating == null) {
            showAlert("Validasi", "Rating harus dipilih.");
            return;
        }
        if (comment == null || comment.isBlank()) {
            showAlert("Validasi", "Komentar tidak boleh kosong.");
            return;
        }

        Review review = new Review();
        review.setWisataId(wisataId);
        review.setUserName(userName);
        review.setRating(rating);
        review.setComment(comment);

        boolean success = reviewDAO.insertReview(review);
        if (success) {
            clearReviewForm();
            loadReviews();
            showAlert("Sukses", "Ulasan berhasil ditambahkan.");
        } else {
            showAlert("Error", "Gagal menambahkan ulasan.");
        }
    }

    private void clearReviewForm() {
        nameField.clear();
        ratingComboBox.getSelectionModel().clearSelection();
        commentArea.clear();
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
