package com.example.tes_labpbo;

import DAO.WisataDAO;
import Model.Wisata;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.List;

/**
 * Controller for MainView.fxml
 * Handles display & searching/filtering wisata list.
 */
public class MainController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField locationField;
    @FXML private VBox wisataListContainer;

    private WisataDAO wisataDAO;

    @FXML
    private void initialize() {
        wisataDAO = new WisataDAO();

        // Load categories into ComboBox with "Semua" option
        List<String> categories = wisataDAO.getAllCategories();
        categories.add(0, "Semua");
        categoryComboBox.getItems().addAll(categories);
        categoryComboBox.getSelectionModel().selectFirst();

        loadAllWisata();
    }

    private void loadAllWisata() {
        List<Wisata> wisataList = wisataDAO.getAllWisata();
        displayWisataList(wisataList);
    }

    private void displayWisataList(List<Wisata> wisataList) {
        wisataListContainer.getChildren().clear();

        if (wisataList.isEmpty()) {
            Label emptyLabel = new Label("Tidak ada destinasi wisata yang ditemukan.");
            wisataListContainer.getChildren().add(emptyLabel);
            return;
        }

        for (Wisata w : wisataList) {
            VBox box = createWisataBox(w);
            wisataListContainer.getChildren().add(box);
        }
    }

    private VBox createWisataBox(Wisata wisata) {
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: #fafafa;");
        box.setSpacing(5);

        Label nameLabel = new Label(wisata.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Label categoryLabel = new Label("Kategori: " + wisata.getCategory());
        categoryLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        Label locationLabel = new Label("Lokasi: " + wisata.getLocation());
        locationLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        Text shortDesc = new Text(wisata.getShortDescription());
        shortDesc.setWrappingWidth(760); // Restrict width for wrapping, adjust based on window size

        box.getChildren().addAll(nameLabel, categoryLabel, locationLabel, shortDesc);

        // Click event to open detail page
        box.setOnMouseClicked(event -> openDetailPage(wisata));

        // Change cursor on hover
        box.setOnMouseEntered(event -> box.setStyle("-fx-border-color: #aaa; -fx-background-color: #f0f8ff;"));
        box.setOnMouseExited(event -> box.setStyle("-fx-border-color: #ddd; -fx-background-color: #fafafa;"));

        return box;
    }

    private void openDetailPage(Wisata wisata) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DetailView.fxml"));
            Parent root = loader.load();

            DetailController controller = loader.getController();
            controller.setWisataId(wisata.getId());

            Stage stage = new Stage();
            stage.setTitle("Detail Wisata: " + wisata.getName());
            stage.setScene(new Scene(root, 700, 500));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman detail wisata.");
        }
    }

    @FXML
    private void onSearch() {
        String name = searchField.getText();
        String category = categoryComboBox.getSelectionModel().getSelectedItem();
        if ("Semua".equals(category)) category = null;
        String location = locationField.getText();

        List<Wisata> filtered = wisataDAO.searchWisata(name, category, location);
        displayWisataList(filtered);
    }

    @FXML
    private void onReset() {
        searchField.clear();
        locationField.clear();
        categoryComboBox.getSelectionModel().selectFirst();
        loadAllWisata();
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
