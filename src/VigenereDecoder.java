import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static util.QuantitiesCalculator.calculateCriterion;
import static util.QuantitiesCalculator.prepareNGramms;

/**
 * Created by Oleg on 19.10.2016.
 */
public class VigenereDecoder extends Decoder {

    public VigenereDecoder(String alphabet, int nGrams) throws IOException {
        super(alphabet, nGrams);
    }

    @Override
    public DecryptionResult decode(String encryptedText) throws IOException {
        System.out.println("encryptedText = " + encryptedText);
        return findKeyLength(encryptedText).stream()
                .map(keyLen -> findKey(encryptedText, keyLen))
                .max((o1, o2) -> (int) Math.ceil(o1.getCriterion() - o2.getCriterion()))
                .get();
    }

    private List<Integer> findKeyLength(String encryptedText) {
        return getMostProbableKeyLength(
                Stream.iterate(2, keyLen -> keyLen + 1)
                        .peek(keyLen -> System.out.print("keyLen = " + keyLen + "  "))
                        .map(keyLen -> calculateIndexOfCoincidence(encryptedText, keyLen))
                        .peek(aDouble -> System.out.println("index = " + aDouble))
                        .limit(alphabet.length() - 2)
                        .collect(Collectors.toList())
        );
    }

    private List<Integer> getMostProbableKeyLength(List<Double> coincidenceIndexes) {
        int maxIndex = -1;
        Double max = Double.MIN_VALUE;
        for (int i = 0; i < coincidenceIndexes.size(); i++) {
            if (coincidenceIndexes.get(i) - max > 0.01) { //TODO: тут аккуратно нужно быть с этим хардкодом
                max = coincidenceIndexes.get(i);
                maxIndex = i + 2; //потому что начинается отстчет с длины = 2
                System.out.println("maxIndex = " + maxIndex);
                System.out.println("max = " + max);
            }
        }
        System.out.println("maxIndex = " + maxIndex);
        System.out.println("max = " + max);
        return Stream.of(maxIndex).collect(Collectors.toList());
    }

    private double calculateIndexOfCoincidence(String encryptedText, int keyLength) {
        List<String> groups = splitForGroups(encryptedText, keyLength);
        System.out.println("groups = " + Arrays.toString(groups.toArray()));
        System.out.println("keyLength = " + keyLength);
        return groups.stream().map(this::calculateIndexOfCoincidence).reduce(Double::sum).orElse(0.0) / groups.size();
    }

    private List<String> splitForGroups(String encryptedText, int keyLength) {
        List<String> groups = new ArrayList<>();
        for (int baseIndex = 0; baseIndex < keyLength; baseIndex++) {
            StringBuilder builder = new StringBuilder();
            Stream.iterate(baseIndex, nextIndex -> nextIndex + keyLength)
                    .limit(encryptedText.length() / keyLength)
                    .map(encryptedText::charAt).forEach(builder::append);
            groups.add(builder.toString());
        }
        return groups;
    }

    private double calculateIndexOfCoincidence(String encryptedGroup) {
        Map<String, Long> codePointCounts = Arrays.stream(encryptedGroup.split("")).collect(
                Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return codePointCounts.values().stream().map(count -> count * (count - 1)).collect(Collectors.summingLong(Long::longValue))
                / (double) (encryptedGroup.length() * (encryptedGroup.length() - 1));
    }

    private DecryptionResult findKey(String encodedText, int keyLen) {
        char[] baseKeyElements = new char[keyLen];
        Arrays.fill(baseKeyElements, 'A');
        String baseKey = new String(baseKeyElements);
        DecryptionResult result = null;

        for (int i = 0; i < keyLen; i++) {
            result = findNextKeyLetter(encodedText, baseKey, i);
            baseKey = result.getKey().substring(0, keyLen);
        }

        boolean wasUpdated = true;
        while (wasUpdated) {
            wasUpdated = false;
            for (int i = 0; i < keyLen; i++) {
                DecryptionResult localResult = findNextKeyLetter(encodedText, baseKey, i);
                if (localResult.getCriterion() > result.getCriterion()) {
                    wasUpdated = true;
                    result = localResult;
                    baseKey = localResult.getKey();
                }
            }
        }
        return result;


    }

    private DecryptionResult findNextKeyLetter(String encodedText, String partialKey, int keyPos) {
        List<DecryptionResult> decryptionResults = new ArrayList<>();
        String nextKey = partialKey;
        for (int i = 1; i < alphabet.length(); i++) {
            nextKey = changeLetterToNext(nextKey, keyPos);
            decryptionResults.add(tryDecode(encodedText, nextKey));
        }
        System.out.println("keyPos = " + keyPos);
        System.out.println("decryptionResults = " + Arrays.toString(decryptionResults.toArray()));
        System.out.println("decryptionResults.size() = " + decryptionResults.size());
        return Collections.min(decryptionResults, (o1, o2) -> (int) Math.ceil(o2.getCriterion() - o1.getCriterion()));
    }

    private DecryptionResult tryDecode(String encodedText, String partialFinishedKey) {
        String decryptedText = decipher(encodedText, partialFinishedKey);
        return new DecryptionResult(partialFinishedKey, calculateCriterion(prepareNGramms(decryptedText, nGrams), theoreticalQuantities), decryptedText);
    }

    public String changeLetterToNext(String partialKey, int keyPos) {
        StringBuilder res = new StringBuilder(partialKey);
        if (partialKey.charAt(keyPos) + 1 > alphabet.charAt(alphabet.length() - 1)) {
            res.setCharAt(keyPos, alphabet.charAt(0));
        } else {
            res.setCharAt(keyPos, (char) (partialKey.charAt(keyPos) + 1));
        }
        return res.toString();
    }

    public static String decipher(String s, String key) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < 65 || s.charAt(i) > 90) { //ASCII character (capital letter)
                throw new IllegalArgumentException("" +
                        "Ciphertext must contain only capital letters");
            }
//subtract shift modularly
            char decyphered = s.charAt(i) - getShift(key, i) < 65 ? (char) ((s.charAt(i) - getShift(key, i)) + 26) : (char) (s.charAt(i) - getShift(key, i));
            builder.append(decyphered);
        }
        return builder.toString();
    }

    private static int getShift(String key, int i) {
        if (key.charAt(i % key.length()) < 65 || key.charAt(i % key.length()) > 90) {
            throw new IllegalArgumentException("" +
                    "Key phrase must contain only capital letters");
        }
        return ((int) key.charAt(i % key.length())) - 65;
    }

    public String transformToFullKey(String partialResultKey) {
        int fullTimes = alphabet.length() / partialResultKey.length();
        int extraLetters = alphabet.length() % partialResultKey.length();
        StringBuilder builder = new StringBuilder(alphabet.length());
        for (int i = 0; i < fullTimes; i++) {
            builder.append(partialResultKey);
        }
        for (int i = 0; i < extraLetters; i++) {
            builder.append(partialResultKey.charAt(i));
        }
        return builder.toString();
    }

}