import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static util.QuantitiesCalculator.*;

/**
 * Created by Oleg on 01.10.2016.
 */
public class Decoder {

    protected String alphabet;

    protected Map<String, Double> theoreticalQuantities;

    protected int nGrams;

    public Decoder(String alphabet, int nGrams) throws IOException {
        this.alphabet = alphabet;
        theoreticalQuantities = getTheoreticalQuantities("resources/" + nGrams + "-gramms.txt");
        this.nGrams = nGrams;
    }

    public DecryptionResult decode(String encryptedText) throws IOException {
        System.out.println("starts decoding...");
        System.out.println("generating base decryption key ...");

        DecryptionResult bestDecryption = decrypt(encryptedText, shuffleKey(), theoreticalQuantities);
        int notChangedKey = 0;
        for (int i = 0; i < 1000; i++) {
            DecryptionResult iterationBase = decrypt(encryptedText, shuffleKey(), theoreticalQuantities);

            while (notChangedKey < 900) {
                DecryptionResult currentDecryption = decrypt(encryptedText, swapKeyElements(iterationBase.getKey()), theoreticalQuantities);
                if (currentDecryption.getCriterion() > iterationBase.getCriterion()) {
                    iterationBase = currentDecryption;
                    notChangedKey = 0;
                } else {
                    notChangedKey++;
                }
            }
            bestDecryption = (iterationBase.getCriterion() > bestDecryption.getCriterion()) ? iterationBase : bestDecryption;
        }
        return bestDecryption;
    }

    protected DecryptionResult decrypt(String encryptedText, String key, Map<String, Double> theoreticalQuantities) {
        String decryptedText = decryptText(encryptedText, key);
        return new DecryptionResult(key, calculateCriterion(prepareNGramms(decryptedText, nGrams), theoreticalQuantities), decryptedText);
    }

    protected String decryptText(String encryptedText, String key) {
        Map<String, String> regroupedKey = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            regroupedKey.put(alphabet.charAt(i) + "", key.charAt(i) + "");
        }
        return Arrays.stream(encryptedText.split("")).map(regroupedKey::get).collect(Collectors.joining());
    }

    private String shuffleKey() {
        List<String> shuffledKey = Arrays.stream(alphabet.split("")).collect(Collectors.toList());
        Collections.shuffle(shuffledKey);
        return shuffledKey.stream().collect(Collectors.joining());
    }

    private String swapKeyElements(String key) {
        return new String(swap(key.toCharArray(), nextInt(), nextInt()));
    }

    private int nextInt() {
        return (int) (Math.random() * 26);
    }

    private char[] swap(char[] chars, int i, int j) {
        char buf = chars[i];
        chars[i] = chars[j];
        chars[j] = buf;
        return chars;
    }
}