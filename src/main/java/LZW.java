import java.util.*;

public class LZW {

    public static String compress(String input){
        HashMap<String,Integer> dictionary = new LinkedHashMap<>();

        String[] data = (input + "").split("");
        StringBuilder out = new StringBuilder();

        ArrayList<String> tempOut = new ArrayList<>();

        String currentChar;
        String phrase = data[0];
        int code = 256;

        for(int i = 1; i < data.length; i++) {
            currentChar = data[i];
            if(dictionary.get(phrase+currentChar) != null) {
                phrase += currentChar;
            }
            else {
                if(phrase.length() > 1) {
                    tempOut.add(Character.toString((char)dictionary.get(phrase).intValue()));
                }
                else {
                    tempOut.add(Character.toString((char)Character.codePointAt(phrase,0)));
                }

                dictionary.put(phrase+currentChar,code);
                code++;
                phrase = currentChar;
            }
        }

        if(phrase.length() > 1){
            tempOut.add(Character.toString((char)dictionary.get(phrase).intValue()));
        }
        else{
            tempOut.add(Character.toString((char)Character.codePointAt(phrase,0)));
        }

        for(String outChar : tempOut){
            out.append(outChar);
        }
        return out.toString();
    }

}