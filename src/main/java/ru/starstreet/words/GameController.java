package ru.starstreet.words;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import ru.starstreet.words.additionalClasses.WordCondition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameController {
    @FXML
    private CheckMenuItem gerMenuItem;
    @FXML
    private CheckMenuItem engMenuItem;
    @FXML
    private CheckMenuItem rusMenuItem;
    @FXML
    private Label checkingResult;
    @FXML
    private Label mainWord;
    @FXML
    private TextField playerInput;
    @FXML
    private TextArea playerOutput;
    @FXML
    private TextArea computerOutput;
    @FXML
    private Label playerPointsLabel;
    @FXML
    private Label computerPointsLabel;

    private int playerPoints;
    private int computerPoints;

    private WordsGame game;
    private String PATH = "src/main/resources/rus/";

    @FXML
    private void initialize() {
        playerPoints = computerPoints = 0;
        game = new WordsGame(PATH + "vocabulary.txt");
        mainWord.setText(game.getMainWord());
        playerPointsLabel.setText("Игрок: 0");
        computerPointsLabel.setText("Компутер: 0");
        computerOutput.clear();
        playerOutput.clear();
    }


    @FXML
    protected void inputWord() {
        String answer = playerInput.getText().toLowerCase();
        if (game.checkWord(answer).equals(WordCondition.GOOD)) {
            playerPoints += answer.length();
            playerPointsLabel.setText("Игрок: " + playerPoints);
            playerOutput.setText(playerOutput.getText() + "\n" + answer);

            playerInput.clear();
            game.addUsedWord(answer);

            String aiAnswer = game.AITurn();
            computerPoints += aiAnswer.length();
            computerPointsLabel.setText("Компутер: " + computerPoints);
            computerOutput.setText(computerOutput.getText() + "\n" + aiAnswer);

            if (computerPoints >= playerPoints && answer.equals(""))
                gameOver("Вы проиграли!");
            else if (playerPoints >= computerPoints && aiAnswer.equals(""))
                gameOver("Вы победили!");

        } else if (game.checkWord(answer).equals(WordCondition.UNKNOWN) &&
                wantsToConfirm())
            confirmWord(answer);
    }

    private void gameOver(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                " Хотите сыграть ещё?",
                new ButtonType("Да", ButtonBar.ButtonData.YES),
                new ButtonType("Нет", ButtonBar.ButtonData.NO)
        );
        alert.setTitle(msg.toUpperCase());

        ButtonBar.ButtonData buttonData = alert
                .showAndWait()
                .get()
                .getButtonData();
        if (buttonData == ButtonBar.ButtonData.YES)
            initialize();
        else
            System.exit(0);


    }

    private boolean wantsToConfirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверенны, что такое слово существует." +
                " Если вы ответите да, то оно будет использовано против вас в следующих играх",
                new ButtonType("Да", ButtonBar.ButtonData.YES),
                new ButtonType("Нет", ButtonBar.ButtonData.NO)
        );
        alert.setTitle("Неизвестное слово!");
        ButtonType buttonType = alert.showAndWait().get();
        return buttonType.getButtonData() == ButtonBar.ButtonData.YES;
    }

    private void confirmWord(String answer) {
        playerInput.setText(answer);
        game.addAnswer(answer);
        try {
            game.saveNewWord(answer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        inputWord();
    }

    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getCharacter().equals("\n"))
            inputWord();
        checkingResult.setText(game.checkWord(playerInput.getText()).toString());
    }

    public void clickExit() {
        System.exit(0);
    }

    public void getRules() {
        getInfo(PATH + "rules.txt");
    }

    public void getAbout() {
        getInfo(PATH + "about.txt");
    }


    private void getInfo(String path) {
        String rules = "";
        try {
            rules = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, rules,
                new ButtonType("Clear", ButtonBar.ButtonData.YES));
        alert.setTitle("info");
        alert.showAndWait();
    }


    public void setToEnglish() {
        rusMenuItem.setSelected(false);
        engMenuItem.setSelected(true);
        gerMenuItem.setSelected(false);
        setLanguage("eng");
    }

    public void setToRussian() {
        rusMenuItem.setSelected(true);
        engMenuItem.setSelected(false);
        gerMenuItem.setSelected(false);
        setLanguage("rus");
    }

    public void setToDeutsch() {
        rusMenuItem.setSelected(false);
        engMenuItem.setSelected(false);
        gerMenuItem.setSelected(true);
        setLanguage("ger");
    }

    private void setLanguage(String language) {
        PATH = "src/main/resources/" + language + "/";
        initialize();
    }
}
