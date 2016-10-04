import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static util.QuantitiesCalculator.*;

/**
 * Created by Oleg on 01.10.2016.
 */
public class Decoder {

    private DecryptionResult bestDecryption;

    private String alphabet;

    private Random random = new Random();

    private List<String> vocabulary;

    public Decoder(String alphabet) {
        this.alphabet = alphabet;
//        vocabulary = Files.readAllLines(Paths.get())
    }

    public DecryptionResult decode(String encryptedText) throws IOException {
        System.out.println("starts decoding...");
        System.out.println("generating base decryption key ...");
        Map<String, Double> theoreticalQuantities = getTheoreticalQuantities("resources/3-gramms.txt");
        bestDecryption = new DecryptionResult(alphabet,
                calculateCriterion(prepareNGramms(decryptText(encryptedText, alphabet), 3),
                        theoreticalQuantities),
                decryptText(encryptedText, alphabet));
        int keyNotChangedIterations = 0;
        int maxKeyNotChangedIterations = 1000;

        while (keyNotChangedIterations < maxKeyNotChangedIterations) {
            String newKey = swapKeyElements(bestDecryption.getKey());
            String decryptedText = decryptText(encryptedText, newKey);
            DecryptionResult currentDecryption = new DecryptionResult(newKey, calculateCriterion(prepareNGramms(decryptedText, 3),
                    theoreticalQuantities), decryptedText);
            if (bestDecryption.getCriterion() < currentDecryption.getCriterion()) {
                bestDecryption = currentDecryption;
                keyNotChangedIterations = 0;
            } else {
                keyNotChangedIterations++;
            }

        }

        return bestDecryption;
    }

    private String swapKeyElements(String key) {
        char[] letters = key.toCharArray();
        int index1 = random.nextInt(26);
        int index2 = random.nextInt(26);
        char buf = letters[index1];
        letters[index1] = letters[index2];
        letters[index2] = buf;
        return new String(letters);
    }

    private String decryptText(String encryptedText, String key) {
        Map<String, String> regroupedKey = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            regroupedKey.put(key.charAt(i) + "", alphabet.charAt(i) + "");
        }
        return Arrays.stream(encryptedText.split("")).map(regroupedKey::get).collect(Collectors.joining());
    }
}