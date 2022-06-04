package ru.starstreet.words;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class GameController {
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

    private final WordsGame game;
    public GameController() {
        playerPoints = computerPoints = 0;
        game = new WordsGame();
    }

    @FXML
    private void initialize(){
        mainWord.setText(game.getMainWord());
        playerPointsLabel.setText("Игрок: 0");
        computerPointsLabel.setText("Компутер: 0");
    }

    @FXML
    protected void inputWord(){
        String answer = playerInput.getText().toLowerCase();
        if (game.checkWord(answer).equals(WordCondition.GOOD)) {
            String text = playerOutput.getText() + "\n" + answer;
            playerOutput.setText(text);
            playerPoints+=answer.length();
            playerPointsLabel.setText("Игрок: " + playerPoints);
            playerInput.setText("");
            game.addUsedWord(answer);

            String aiAnswer = game.AITurn();
            computerOutput.setText(computerOutput.getText() + "\n" + aiAnswer);
            computerPoints+=aiAnswer.length();
            computerPointsLabel.setText("Компутер:" + computerPoints);
        } else
            if (game.checkWord(answer).equals(WordCondition.UNKNOWN) &&
            wantsToConfirm())
                confirmWord(answer);
    }

    private boolean wantsToConfirm() {
        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверенны, что такое слово существует." +
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
}