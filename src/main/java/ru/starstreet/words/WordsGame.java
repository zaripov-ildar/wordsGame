package ru.starstreet.words;

import ru.starstreet.words.additionalClasses.WordCondition;
import ru.starstreet.words.additionalClasses.WordStructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class WordsGame {

    public final String PATH;
    private final String mainWord;
    private final List<String> answers;
    private final WordStructure structure;
    private final List<String> usedWords;

    WordsGame(String path) {
        PATH = path;
        List<String> vocabulary;
        try {
            vocabulary = downloadVocabulary();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mainWord = chooseMainWord(vocabulary);
        structure = new WordStructure(mainWord);
        usedWords = new ArrayList<>();
        answers = downloadAnswers(vocabulary);
        System.out.println(answers);
        System.out.println(answers.size());
    }

    private String chooseMainWord(List<String> vocabulary) {
        Random rnd = new Random();
        String result;
        do {
            result = vocabulary.get(rnd.nextInt(vocabulary.size()));
        }
        while (result.length() < 10);
        return result;
    }

    public void addUsedWord(String word) {
        answers.remove(word);
        usedWords.add(word);
    }

    public void addAnswer(String word) {
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
        if (answers.isEmpty())
            return "";
        Random rnd = new Random();
        int index = rnd.nextInt(answers.size());
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
        Set<String> set = new HashSet<>(result);
        set.remove(mainWord);
        return new ArrayList<>(set);
    }

    public String getMainWord() {
        return mainWord;
    }

    public WordCondition checkWord(String word) {
        if (word.equals(mainWord))
            return WordCondition.WRONG;
        if (word.equals(""))
            return WordCondition.GOOD;
        if (!structure.contains(word))
            return WordCondition.WRONG;
        if (usedWords.contains(word))
            return WordCondition.USED;
        if (!answers.contains(word))
            return WordCondition.UNKNOWN;
        return WordCondition.GOOD;
    }

    public void saveNewWord(String answer) throws IOException {

        Files.write(Paths.get(PATH), (answer + "\n").getBytes(), StandardOpenOption.APPEND);
    }
}
