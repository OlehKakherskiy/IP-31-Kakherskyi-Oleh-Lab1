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
        for (Map.Entry<String, Double> entry : nGrammsQuantities.entrySet()) {
            nGrammsQuantities.put(entry.getKey(), entry.getValue() / all);
        }

        return nGrammsQuantities;
    }

    public static Map<String, Double> calculateEmpiricalQuantities(String encryptedText) {
//        Map<String, Double> result = new HashMap<>();
//        for (int i = 1; i <= 5; i++) {
//            result.putAll(calculateEmpiricalQuantitiesForNGramms(encryptedText, i));
//        }
//        return result;
        return calculateEmpiricalQuantitiesForNGramms(encryptedText, 1);
    }

    private static Map<String, Double> calculateEmpiricalQuantitiesForNGramms(String encryptedText, int nGramm) {
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
