package ru.starstreet.words;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordsGame {
    public static final String PATH = "src/main/resources/vocabulary.txt";
    private final String mainWord;
    private final List<String> answers;

    private final WordStructure structure;

    private final List<String> usedWords;

    WordsGame() {
        List<String> vocabulary;
        try {
            vocabulary = downloadVocabulary();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(vocabulary);
        mainWord = chooseMainWord(vocabulary);
        System.out.println("mainword " + mainWord);
        structure = new WordStructure(mainWord);
        usedWords = new ArrayList<>();
        answers = downloadAnswers(vocabulary);
        System.out.println(answers);


    }

    private String chooseMainWord(List<String> vocabulary) {
        Random rnd = new Random();
        String result;
        do {
            result = vocabulary.get(rnd.nextInt(vocabulary.size()));
        }
        while (result.length() < 6);
        return result;
    }

    public void addUsedWord(String word){
        answers.remove(word);
        usedWords.add(word);
    }
    public void addAnswer(String word){
        answers.add(word);
    }
    private List<String> downloadVocabulary() throws IOException {
        Path path = Path.of(PATH);
        return Files.readAllLines(path);
    }

    private List<String> downloadAnswers(List<String> allWords) {
        return getCorrectWords(allWords);
    }

    public String AITurn() {
        Random rnd = new Random();
        int index = rnd.nextInt(answers.size())-1;
        if (index < 0) return "";
        String result = answers.remove(index);
        usedWords.add(result);
        return result;
    }


    private List<String> getCorrectWords(List<String> allWords) {
        List<String> result = new ArrayList<>();
        for (String word : allWords) {
            if (structure.contains(word))
                result.add(word);
        }
        return result;
    }

    public String getMainWord() {
        return mainWord;
    }

    public WordCondition checkWord(String word) {
        if (!structure.contains(word))
            return WordCondition.WRONG;
        if (usedWords.contains(word))
            return WordCondition.USED;
        if (!answers.contains(word))
            return WordCondition.UNKNOWN;
        return WordCondition.GOOD;
    }

    public void saveNewWord(String answer) throws IOException {

        Files.write(Paths.get(PATH), answer.getBytes(), StandardOpenOption.APPEND);
    }
}
