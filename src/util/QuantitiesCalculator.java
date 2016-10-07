package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Created by Oleg on 01.10.2016.
 */
public class QuantitiesCalculator {

    public static Map<String, Double> getTheoreticalQuantities(String fileName) throws IOException {
        Map<String, Double> nGrammsQuantities;
        nGrammsQuantities = Files.lines(Paths.get(fileName))
                .map(String::toLowerCase).map(s -> s.split(" "))
                .collect(toMap(o -> o[0], o -> Double.parseDouble(o[1])));
        double all = nGrammsQuantities.values().stream().mapToDouble(Double::doubleValue).sum();
        return nGrammsQuantities.entrySet().stream().collect(toMap(Map.Entry::getKey, stringDoubleEntry -> Math.log10(stringDoubleEntry.getValue() / all)));

    }

    public static double calculateCriterion(List<String> currentNGrams, Map<String, Double> targetQuantities) {
        return currentNGrams.stream()
                .mapToDouble(key -> targetQuantities.containsKey(key) ? targetQuantities.get(key) : 0.0)
                .reduce(Double::sum).orElse(0.0);
    }

    public static List<String> prepareNGramms(String decryptedText, int nGramm) {

        List<String> result = new ArrayList<>(decryptedText.length() / nGramm);
        for (int i = 0; i < decryptedText.length() - nGramm + 1; i++) {
            result.add(decryptedText.substring(i, i + nGramm));
        }
        return result;

    }
}
