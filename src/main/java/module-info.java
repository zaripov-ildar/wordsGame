module ru.starstreet.words {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.starstreet.words to javafx.fxml;
    exports ru.starstreet.words;
}