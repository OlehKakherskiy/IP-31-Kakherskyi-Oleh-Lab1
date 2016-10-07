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
                .map(String::toLowerCase)
                .map(s -> s.split(" "))
                .collect(toMap(o -> o[0], o -> Double.parseDouble(o[1])));
        double all = nGrammsQuantities.values().stream().mapToDouble(Double::doubleValue).sum();
        return nGrammsQuantities.entrySet().stream().collect(toMap(Map.Entry::getKey, stringDoubleEntry -> Math.log10(stringDoubleEntry.getValue() / all)));

//        return nGrammsQuantities;
    }

    public static double calculateCriterion(List<String> currentNGrams, Map<String, Double> targetQuantities) {
//        Map<String, Double> nGramsQuantities = currentNGrams.stream()
//                .collect(groupingBy(Function.identity(), Collectors.collectingAndThen(Collectors.counting(),
//                        count -> count / (double) currentNGrams.size())));
//        System.out.println(nGramsQuantities.entrySet().stream().map(Map.Entry::getValue).collect(toList()).stream().sorted(Comparator.comparing(Double::doubleValue).reversed()).collect(toList()));
//        double criterion = 0;
//        return targetQuantities.entrySet().stream()/*.filter(entry -> nGramsQuantities.containsKey(entry.getKey()))*/
//                .map(entry -> chiCriterion(nGramsQuantities.get(entry.getKey()), entry.getValue()))
//                .reduce(Double::sum).get();
//        for (Map.Entry<String, Double> targetTrigram : targetQuantities.entrySet()) {
//            criterion += nGramsQuantities.
//        }

        return currentNGrams.stream()
                .mapToDouble(key -> targetQuantities.containsKey(key) ? targetQuantities.get(key) : 0.0)
                .reduce(Double::sum).orElse(0.0);
    }

//    private static double chiCriterion(Double current, double expected) {
//        current = (current == null) ? 0 : current;
//        return Math.pow(current - expected, 2.0) / expected;
//    }

    public static List<String> prepareNGramms(String decryptedText, int nGramm) {
//        System.out.println("decryptedText = " + decryptedText);

        List<String> result = new ArrayList<>(decryptedText.length() / nGramm);
        for (int i = 0; i < decryptedText.length() - nGramm + 1; i++) {
            result.add(decryptedText.substring(i, i + nGramm));
        }
//        System.out.println("result = " + result);
        return result;

    }
}
