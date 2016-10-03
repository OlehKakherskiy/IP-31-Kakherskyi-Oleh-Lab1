import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oleg on 01.10.2016.
 */
public class Decoder {

    private DecryptionResult bestDecryption;

    private List<String> vocabulary;

    private String alphabet;

    private Random random = new Random();

    private Set<Map<String, String>> generatedKeys = new HashSet<>();

    public Decoder(List<String> vocabulary, String alphabet) {
        this.vocabulary = vocabulary;
        this.alphabet = alphabet;
    }

    public DecryptionResult decode(String encryptedText) throws IOException {
        System.out.println("starts decoding...");
        System.out.println("generating base decryption key ...");
        bestDecryption = getBaseDecryption(encryptedText);
        int collisionsCount = 0;
        DecryptionResult currentDecryption = bestDecryption;
        while (!currentDecryption.isRightDecryption()) {
            Map<String, String> newKey = (collisionsCount >= 1000) ? shuffleKey() : generateKey(bestDecryption.getKey());
            if (generatedKeys.contains(newKey)) {
//                System.out.println("contains key");
                collisionsCount++;
                continue;
            }
            collisionsCount = 0;
            generatedKeys.add(newKey);
            System.out.println("keys generated = " + generatedKeys.size());
            currentDecryption =
                    DecryptionAnalyzer.isRightDecryption(decryptText(encryptedText, newKey), newKey, vocabulary);
            System.out.println("currentDecryption = " + currentDecryption);
            System.out.println("bestDecryption = " + bestDecryption);
            chooseBetterDecryption(bestDecryption, currentDecryption);
        }
        return bestDecryption;
    }

    private Map<String, String> shuffleKey() {
        try {
            PrintWriter pw = new PrintWriter(new File("res.txt"));
            pw.println(bestDecryption.toString());
            bestDecryption = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> letters = Arrays.stream(alphabet.split("")).collect(Collectors.toList());
        List<String> shuffledLetters = letters.stream().collect(Collectors.toList());
        Collections.shuffle(shuffledLetters);
        Map<String, String> key = new HashMap<>();
        for (int i = 0; i < letters.size(); i++) {
            key.put(letters.get(i), shuffledLetters.get(i));
        }
        System.out.println("shuffled key" + key);
        return key;
    }

    private void chooseBetterDecryption(DecryptionResult prevDecryption, DecryptionResult currentDecryption) {
        if(prevDecryption == null){
            System.out.println("After shuffle there's no best decryption");
            bestDecryption = currentDecryption;
            return;
        }
        if (currentDecryption.isRightDecryption() || currentDecryption.getChiCriterion() <= prevDecryption.getChiCriterion()) {
            bestDecryption = currentDecryption;
            System.out.println("new base decryption");
        } else {
//            System.out.println("base decryption is left: " + prevDecryption);
//        else
        }
//            bestDecryption = getChanceToWorseDecryption() ? currentDecryption : prevDecryption;
//        }
//        System.out.println("prev decryption chi = " + prevDecryption.getChiCriterion());
//        System.out.println("current decryption chi = " + currentDecryption.getChiCriterion());
    }

//    private boolean getChanceToWorseDecryption() {
//        return Math.random() <= 0.01;
//    }

    private Map<String, String> generateKeyByEmpiricalQuantities(Map<String, Double> empiricalQuantities) {
        Map<String, String> targetKey = new HashMap<>();
        String sortedLettersByTheoreticalQuantity = "etaoinsrhldcumfgpwybvkjxzq";
        int i = 0;
        for (String letter : QuantitiesCalculator.sortLettersByEmpiricalQuantities(empiricalQuantities)) {
            targetKey.put(letter, sortedLettersByTheoreticalQuantity.charAt(i++) + "");
        }
        System.out.println("generated base key: " + targetKey.values().stream().collect(Collectors.joining()));
        return targetKey;
    }

    private Map<String, String> generateKey(Map<String, String> baseKey) {
        return shuffleKeyElements(baseKey);
    }

    private Map<String, String> shuffleKeyElements(Map<String, String> key) {
        Map<String, String> newKey = new HashMap<String, String>() {{
            putAll(key);
        }};
        String letter1 = getRandomElementFromKey(key);
        String letter2 = getRandomElementFromKey(key);
//        System.out.println("letter1 = " + letter1 + " letter2 = " + letter2);
        if (letter1.equals(letter2))
            return key;
        String buffer = newKey.get(letter1);
        newKey.put(letter1, newKey.get(letter2));
        newKey.put(letter2, buffer);
//        System.out.println("old key:           " + key.values().stream().collect(Collectors.joining()));
//        System.out.println("generated new key: " + newKey.values().stream().collect(Collectors.joining()));

        return newKey;
    }

    private String getRandomElementFromKey(Map<String, String> key) {
        String elem = null;
        do {
            int index = random.nextInt(alphabet.length());
            elem = alphabet.charAt(index) + "";
        } while (!key.containsKey(elem));
        return elem;
    }

    private String decryptText(String encryptedText, Map<String, String> key) {
        return Arrays.stream(encryptedText.split("")).map(key::get).collect(Collectors.joining());
    }

    private DecryptionResult getBaseDecryption(String encryptedText) {
        Map<String, Double> empiricalQuantities = QuantitiesCalculator.calculateEmpiricalQuantities(encryptedText, 1);
        System.out.println(empiricalQuantities.size());
        Map<String, String> key = generateKeyByEmpiricalQuantities(empiricalQuantities);
        return DecryptionAnalyzer.isRightDecryption(decryptText(encryptedText, key), key, vocabulary);
    }


}
