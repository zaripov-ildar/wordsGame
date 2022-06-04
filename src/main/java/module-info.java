module ru.starstreet.words {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.starstreet.words to javafx.fxml;
    exports ru.starstreet.words;
    exports ru.starstreet.words.additionalClasses;
    opens ru.starstreet.words.additionalClasses to javafx.fxml;
}