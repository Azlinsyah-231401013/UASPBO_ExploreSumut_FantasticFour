module com.example.tes_labpbo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.tes_labpbo to javafx.fxml;
    exports com.example.tes_labpbo;
}