import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oleg on 01.10.2016.
 */
public class Decoder {

    private DecryptionResult baseDecryption;

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
        baseDecryption = getBaseDecryption(encryptedText);
        System.out.println(baseDecryption);
        while (!baseDecryption.isRightDecryption()) {
            Map<String, String> newKey = generateKey(baseDecryption.getKey());
            if (generatedKeys.contains(newKey)) {
                System.out.println("contains key");
                continue;
            }
            generatedKeys.add(newKey);
            System.out.println("keys generated = " + generatedKeys.size());
            DecryptionResult currentDecryption =
                    DecryptionAnalyzer.isRightDecryption(decryptText(encryptedText, newKey), newKey, vocabulary);
            System.out.println(currentDecryption);
            chooseBetterDecryption(baseDecryption, currentDecryption);
        }
        return baseDecryption;
    }

    private void chooseBetterDecryption(DecryptionResult prevDecryption, DecryptionResult currentDecryption) {
        System.out.println("Choosing better decryption...");
        if (currentDecryption.isRightDecryption() || currentDecryption.decryptedLetters() >= prevDecryption.decryptedLetters()) {
            this.baseDecryption = currentDecryption;
        } else {
            System.out.println("Даём шанс худшему ключу стать базовым");
            double baseKeyCompletion = prevDecryption.decryptedLetters() / alphabet.length();
            double currentKeyCompletion = currentDecryption.decryptedLetters() / alphabet.length();
            if (shouldBeChanged(currentKeyCompletion, baseKeyCompletion)) {
                this.baseDecryption = currentDecryption;
                System.out.println("Худший ключ стал базовым");
            } else {
                System.out.println("Базовый ключ не изменился");
                this.baseDecryption = prevDecryption;
            }
        }
    }

    private boolean shouldBeChanged(double currentKeyCompletion, double baseKeyCompletion) {
        double randomValue = Math.random();
        return randomValue >= currentKeyCompletion && randomValue <= baseKeyCompletion;
    }

    private Map<String, String> generateKeyByEmpiricalQuantities(Map<String, Double> empiricalQuantities) {
        Map<String, String> targetKey = new HashMap<>();
        String sortedLettersByTheoreticalQuantity = "etaoinsrhldcumfgpwybvkjxzq";
        List<String> sortedLettersByEmpiricalQuantity =
                empiricalQuantities.entrySet().stream()
                        .sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
        int i = 0;
        for (String letter : sortedLettersByEmpiricalQuantity) {
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
        System.out.println("letter1 = " + letter1+" letter2 = "+letter2);
        if (letter1.equals(letter2))
            return key;
        String buffer = newKey.get(letter1);
        newKey.put(letter1, newKey.get(letter2));
        newKey.put(letter2, buffer);
        System.out.println("old key:           " + key.values().stream().collect(Collectors.joining()));
        System.out.println("generated new key: " + newKey.values().stream().collect(Collectors.joining()));

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
        Map<String, Double> empiricalQuantities = QuantitiesCalculator.calculateEmpiricalQuantities(encryptedText);
        Map<String, String> key = generateKeyByEmpiricalQuantities(empiricalQuantities);
        return DecryptionAnalyzer.isRightDecryption(decryptText(encryptedText, key), key, vocabulary);
    }


}
