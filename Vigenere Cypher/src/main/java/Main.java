import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    //http://practicalcryptography.com/cryptanalysis/letter-frequencies-various-languages/english-letter-frequencies/
    static double[] letterProbabilitiesEng = { 8.55, 1.60, 3.16, 3.87, 12.10, 2.18, 2.09, 4.96, 7.33, 0.22, 0.81, 4.21, 2.53, 7.17, 7.47, 2.07, 0.10, 6.33, 6.73, 8.94, 2.68, 1.06, 1.83, 0.19, 1.72, 0.11};
    static String key;
    static String encryptedText;
    static double superior = 0.075;
    static double inferior = 0.055;

    /**
     * method used to generate a random key using Math.random()
     * with length between [3,9]
     */
    public static void generateRandomKey(){

        int lengthKey = (int) ((Math.random() * (10 - 3) ) + 3);
        StringBuilder keyToGenerate = new StringBuilder();

        for( int count = 0 ; count < lengthKey; count++ ){

            int generatedChar = (int)( Math.random() * 25 );
            keyToGenerate.append((char)(generatedChar + 65));
        }

        key = keyToGenerate.toString();
    }

    /**
     * @param character the character that needs to be shifted
     * @param charKey the character that is added to the first char
     * @return the character after encrypting
     */
    public static char changeChar(char character, int charKey){

        int codeCharacter = ((int)character) - 65;
        charKey = charKey - 65;

        codeCharacter += charKey;
        codeCharacter = codeCharacter % 26;

        return (char)(codeCharacter+65);
    }

    /**
     * method used to encrypt a text using a given key
     * @param plaintext the text that need to be encrypted
     */
    public static void filterAndEncryptText(String plaintext){

        try {
            FileReader reader  = new FileReader(plaintext);
            FileWriter writer  = new FileWriter("filtered.txt");
            FileWriter writer2 = new FileWriter("encrypted.txt");

            StringBuilder coded = new StringBuilder();
            int data = reader.read();
            int counter = 0;

            while( data != -1 ){

                char character = (char) data;

                if( character >= 'a' && character <= 'z' ){

                    character = Character.toUpperCase(character);
                    writer.write(character);

                    int charKey = key.charAt(counter);
                    character = changeChar(character, (char)charKey);

                    coded.append(character);
                    writer2.write(character);

                    counter = ( counter + 1 ) % key.length();
                }
                else if( character >= 'A' && character <= 'Z' ){

                    writer.write(character);

                    int charKey = key.charAt(counter);
                    character = changeChar(character, (char)charKey);

                    coded.append(character);
                    writer2.write(character);

                    counter = ( counter + 1 ) % key.length();
                }

                data = reader.read();
            }

            encryptedText = coded.toString();
            reader.close();
            writer.close();
            writer2.close();

        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void encryptText(String filename){

        try {
            FileReader reader = new FileReader(filename);

            StringBuilder coded = new StringBuilder();
            int data = reader.read();

            while( data != -1 ){

                char character = (char) data;
                coded.append(character);
                data = reader.read();
            }
            encryptedText = coded.toString();
            reader.close();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * method used to decrypt a text using a given key
     * @param key the key that is used for decrypting
     */
    public static void decryptText(String key) {

        int countKey = 0;

        for (int position = 0; position < encryptedText.length(); position++) {

            int decryptedChar;
            int characterKey = (int) key.charAt(countKey);
            characterKey -= 65;
            int encryptedChar = (int) encryptedText.charAt(position);
            encryptedChar -= 65;

            if (encryptedChar - characterKey < 0){
                decryptedChar = 26 + ( encryptedChar - characterKey );
            }
            else{
                decryptedChar = encryptedChar - characterKey;
            }

            decryptedChar = decryptedChar + 65;
            System.out.print((char)decryptedChar);
            countKey = (countKey + 1) % key.length();
        }
    }

    /**
     * method used to compute the index of coincidence in a text
     *
     * first it computes the frequencies of the letters in a given text
     * then it computes the IC, using the formula: sum(i=0;i<26): ( freq(i)*(freq(i)-1) )/ ( text.length() * ( text.length() - 1) )
     *
     * @param text the text over is computed the index
     * @return a double value that stores the index
     */
    public static double indexOfCoincidence(String text){

        ArrayList<Integer> frequencies = new ArrayList<>();
        for( int letterPosition = 0 ; letterPosition < 26 ; letterPosition++ )
            frequencies.add(0);

        for( int positionLetter = 0 ; positionLetter < text.length(); positionLetter++ ){

            int letter = (int)text.charAt(positionLetter) - 65;
            frequencies.set(letter, frequencies.get(letter) + 1 );
        }

        double IC = 0;

        for( int positionLetter = 0 ; positionLetter < 26; positionLetter++ ){

            double fraction1 = frequencies.get(positionLetter) * (frequencies.get(positionLetter) - 1) ;
            double fraction2 = text.length() * ( text.length() - 1);
            IC += ( fraction1 / fraction2 );
        }

        return IC;
    }

    /**
     * http://practicalcryptography.com/cryptanalysis/text-characterisation/index-coincidence/
     * @return the measure of roughness
     */
    public static double measureOfRoughness(){

        ArrayList<Integer> frequencies = new ArrayList<>();
        for( int letterPosition = 0 ; letterPosition < 26 ; letterPosition++ )
            frequencies.add(0);

        for( int positionLetter = 0 ; positionLetter < encryptedText.length(); positionLetter++ ){

            int letter = (int)encryptedText.charAt(positionLetter) - 65;
            frequencies.set(letter, frequencies.get(letter) + 1 );
        }

        double MR = 0;
        for( int letterPosition = 0 ; letterPosition < 26 ; letterPosition++ ){

            double probability = (double)frequencies.get(letterPosition) / encryptedText.length();
            MR += probability * probability;
        }

        MR = MR - (double)1/26;
        superior = 0.065 + MR;
        inferior = 0.065 - MR;

        return MR;
    }

    /**
     * method creates subtexts for IC
     * it concatenates every character starting from startPosition, until the last, with a given step
     *
     * @param encryptedText text from where to create subtexts
     * @param startPosition the starting position
     * @param step the step
     * @return an subtexts from the given text
     */
    public static String generateTextForIC(String encryptedText, int startPosition, int step){

        StringBuilder text = new StringBuilder();

        for( int position = startPosition ; position < encryptedText.length() ; position += step ) {
            text.append(encryptedText.charAt(position));
        }

        return text.toString();
    }

    /**
     * method tries to find the length of the key by comparing the IC from the subtexts with ~= 0.065
     * if all the subtexts are near 0.065, the length might be good
     * @param encryptedText - the encrypted text, on which we compute the length
     * @return the length of the key
     */
    public static int lengthOfKey(String encryptedText){

        int length;
        for( length = 3 ; length < 10 ; length++ ){

            //System.out.println("Checking for length " + length);
            boolean goodLength = true;
            for( int m = 0 ; m < length ; m++ ){

                String textM = generateTextForIC(encryptedText, m, length);
                double IC = indexOfCoincidence(textM);
                //System.out.println(IC);

                if( IC < inferior || IC > superior ){
                    goodLength = false;
                    break;
                }
            }
            if( goodLength ){
                break;
            }
            //System.out.println();
        }

        return length;
    }

    /**
     * the method tries to compute mutual index of coincidence for a given text and a normal text in english
     * for the given text, we compute the frequencies of the letters
     * for the normal text in english, we can use some predetermined frequencies
     *
     * for the MIC, we use the formula: sum(i=0;i<26): P_i * freq(i) / text.length()
     *
     * @param textBeta given text
     * @return an approximation for the mutual index of coincidence
     */
    public static double MIC(String textBeta){

        ArrayList<Double> frequenciesBeta  = new ArrayList<>();
        for( int letter = 0 ; letter < 26 ; letter++ ){
            frequenciesBeta.add(0.0);
        }

        for( int character = 0 ; character < textBeta.length(); character++ ){

            int letter = (int)textBeta.charAt(character) - 65;
            frequenciesBeta.set(letter, frequenciesBeta.get(letter) + 1 );
        }

        double MIC = 0;

        for( int letter = 0 ; letter < 26 ; letter++ ){

            double fraction = frequenciesBeta.get(letter) / textBeta.length();
            MIC += letterProbabilitiesEng[letter] * fraction / 100;
        }

        return MIC;
    }

    /**
     * method shift every character using the "changeChar" method
     * giving as arguments the current char and the "charKey" from parameters
     *
     * @param text the text that needs to be shifted
     * @param charKey the char that is used for shifting
     * @return the text after it was shifted
     */
    public static String shiftText(String text, char charKey){

        StringBuilder shiftedText = new StringBuilder(text);

        for( int position = 0; position < shiftedText.length() ; position++ ){

            shiftedText.setCharAt(position, changeChar( shiftedText.charAt(position), charKey ));
        }

        return shiftedText.toString();
    }

    /**
     * for every position of the key, the algorithm create a substring from a specific starting point
     * selecting letters using a step equal to length of the key
     *
     * and for every substring, it computes the mutual index of coincidence
     * for 2 normal texts in english, the MIC should be ~= 0.065
     * and so, we call the method "MIC", giving as parameters the shifted text, in hopes that it has "normal" frequencies
     * if the "MIC" is around "0.065", we can try the letter as a component of the key
     *
     * @param length the length of the key
     * @param encryptedText the encrypted text
     * @return a possible key
     */
    public static String determineKey(int length, String encryptedText){

        StringBuilder foundKey = new StringBuilder();

        for(int m = 0 ; m < length ; m++ ) {

            boolean goodChar = false;
            int s = -1;
            int letter;
            do {
                s = s + 1;

                String shiftedText = generateTextForIC(encryptedText, m, length);

                shiftedText = shiftText(shiftedText, (char) (s + 65));

                double MIC = MIC(shiftedText);

                letter = (26 - s) % 26;
                //System.out.println("Caracterul: " + (char) (letter + 65) + " are MIC: " + MIC);

                if( MIC > inferior && MIC < superior )
                    goodChar = true;

            } while (!goodChar);

            foundKey.append((char) (letter + 65));
        }

        return foundKey.toString();
    }

    public static void main(String[] args) {

        Scanner myScanner = new Scanner(System.in);
        System.out.println("1) Generate random key:");
        System.out.println("2) Introduce a key:");
        System.out.println("3) Decrypt a text:");

        int option = myScanner.nextInt();

        if (option == 3) {

            System.out.println("Introduce the name of the file: ");
            String fileName = myScanner.next();

            encryptText(fileName);
            int foundLength = lengthOfKey(encryptedText);
            String foundKey = determineKey(foundLength, encryptedText);

            System.out.println("Found key length: " + foundLength);
            System.out.println("Found key: " + foundKey);

            System.out.println("The decrypted text is:");
            decryptText(foundKey);
        } else {

            if (option == 1) {
                generateRandomKey();
                System.out.println(key);
            } else if (option == 2) {
                System.out.println("Introduce a key: ");
                key = myScanner.next();
                System.out.println(key);
            } else {
                System.out.println("Option incorrect!");
                System.exit(0);
            }

            filterAndEncryptText("plaintext.txt");

            int foundLength = lengthOfKey(encryptedText);
            String foundKey = determineKey(foundLength, encryptedText);

            System.out.println("Key length is: " + key.length() + " | Found key length: " + foundLength);
            System.out.println("Original Key: " + key + " | Found key: " + foundKey);

            System.out.println("The decrypted text is:");
            decryptText(foundKey);
        }
    }
}