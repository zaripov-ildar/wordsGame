package ru.starstreet.words;

import java.util.HashMap;
import java.util.Map;

public class WordStructure {
    private final Map<Character, Integer> structure;

    public WordStructure(String word) {
        structure = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (structure.containsKey(ch))
                structure.put(ch, structure.get(ch)+1);
            else
                structure.put(ch, 1);
        }
    }

    private Map<Character, Integer> getStructure(){
        return structure;
    }
    public boolean contains(String word){
        WordStructure struct = new WordStructure(word);
        for (Map.Entry<Character, Integer> entry: struct.getStructure().entrySet()){
            char ch = entry.getKey();
            if (!(this.structure.containsKey(ch) &&
            this.structure.get(ch) >= entry.getValue()))
                return false;
        }
        return true;
    }
}
