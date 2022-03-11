import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW {

    public static List<Integer> encode(String text) {
        int dictSize = 256;
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < dictSize; i++) {
            dictionary.put(String.valueOf((char) i), i);
        }

        String foundChars = "";
        List<Integer> result = new ArrayList<>();
        for (char character : text.toCharArray()) {
            String charsToAdd = foundChars + character;
            if (dictionary.containsKey(charsToAdd)) {
                foundChars = charsToAdd;
            } else {
                result.add(dictionary.get(foundChars));
                dictionary.put(charsToAdd, dictSize++);
                foundChars = String.valueOf(character);
            }
        }
        if (!foundChars.isEmpty()) {
            result.add(dictionary.get(foundChars));
        }
        return result;
    }

}