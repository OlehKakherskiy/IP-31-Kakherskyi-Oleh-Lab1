import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Created by Oleg on 01.10.2016.
 */
public class QuantitiesCalculator {

    public static Map<String, Double> getTheoreticalQuantities(String fileName) throws IOException {
        Map<String, Double> nGrammsQuantities = new HashMap<>();
        Files.lines(Paths.get(fileName)).map(s -> s.split(" ")).forEach(strings -> {
            nGrammsQuantities.put(strings[0].toLowerCase(), Double.parseDouble(strings[1]));
        });
        double all = nGrammsQuantities.values().stream().mapToDouble(Double::doubleValue).sum();
        for (Map.Entry<String, Double> entry : nGrammsQuantities.entrySet()) {
            nGrammsQuantities.put(entry.getKey(), entry.getValue() / all);
        }

        return nGrammsQuantities;
    }

    public static double calculateChiCriterion(Map<String, Double> currentQuantities, Map<String, Double> targetQuantities) {
        int chi = 0;
        for (Map.Entry<String, Double> entry : targetQuantities.entrySet()) {
            Double current = currentQuantities.get(entry.getKey());
            current = (current == null) ? 0 : current;
            chi += Math.pow(current - entry.getValue(), 2.0) / entry.getValue();
        }
        return chi;
    }

    public static Map<String, Double> calculateEmpiricalQuantities(String text) {
        Map<String, Double> result = new HashMap<>();
        for (int i = 2; i <= 3; i++) {
            result.putAll(calculateEmpiricalQuantities(text, i));
        }
        return result;
//        return calculateEmpiricalQuantitiesForNGramms(text, nGramm);
    }

    public static List<String> sortLettersByEmpiricalQuantities(Map<String, Double> empiricalQuantities) {
        return empiricalQuantities.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static Map<String, Double> calculateEmpiricalQuantities(String encryptedText, int nGramm) {
        Stream<String> targetStream = (nGramm == 1)
                ? Arrays.stream(encryptedText.split(""))
                : prepareNGramms(encryptedText, nGramm).stream();
        return targetStream.collect(groupingBy(Function.identity(),
                collectingAndThen(counting(), count -> count / (double) encryptedText.length())));
    }

    private static List<String> prepareNGramms(String encryptedText, int nGramm) {
        List<String> result = new ArrayList<>(encryptedText.length() / nGramm);
        for (int i = 0; i < encryptedText.length() - nGramm; i++) {
            result.add(encryptedText.substring(i, i + nGramm));
        }
        return result;
    }
}
