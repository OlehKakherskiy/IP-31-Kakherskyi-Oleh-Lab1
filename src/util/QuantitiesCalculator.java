package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
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
        nGrammsQuantities.entrySet().stream().limit(1000).collect(toMap(Map.Entry::getKey, stringDoubleEntry -> stringDoubleEntry.getValue() / all));

        return nGrammsQuantities;
    }

    public static double calculateCriterion(List<String> currentNGrams, Map<String, Double> targetQuantities) {
        return currentNGrams.stream()
                .mapToDouble(key -> targetQuantities.containsKey(key) ? targetQuantities.get(key) : 0.0)
                .reduce(Double::sum).orElse(0.0);
    }

    public static List<String> prepareNGramms(String decryptedText, int nGramm) {
        List<String> result = new ArrayList<>(decryptedText.length() / nGramm);
        for (int i = 0; i < decryptedText.length() - nGramm; i++) {
            result.add(decryptedText.substring(i, i + nGramm));
        }
        return result;
    }
}
