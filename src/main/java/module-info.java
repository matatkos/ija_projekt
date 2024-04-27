module org.example.ija_projekt {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ija_projekt to javafx.fxml;
    exports org.example.ija_projekt;
}