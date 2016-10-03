import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleg on 01.10.2016.
 */
public class DecryptionAnalyzer {

    public static Map<String, Double> nGrammsQuantities;

    public static DecryptionResult isRightDecryption(String decryptedText, Map<String, String> key, List<String> vocabulary) {
//        System.out.println("decryptedText = " + decryptedText);
        List<String> decryptedWords = new ArrayList<>();
        boolean isComplete = true;
        outer:
        while (!decryptedText.isEmpty()) {
//            for(String str: vocabulary){
//                if(str.isEmpty())
//                    continue ;
//                str = str.toLowerCase();
//                if(decryptedText.startsWith(str.toLowerCase())){
//                    System.out.println("decrypted word = " + str);
//                    decryptedWords.add(str);
//                    decryptedText = decryptedText.substring(str.length());
//                    continue outer;
//                }
//            }
//            isComplete = false;
//            break;
            String decryptedWord = vocabulary.stream().filter(decryptedText::startsWith).findFirst().orElse("");
//            System.out.println("decryptedWord = " + decryptedWord);
            if (decryptedWord.isEmpty()) {
                isComplete = false;
                break;
            } else {
//                System.out.println("decryptedText = " + decryptedText.substring(decryptedWord.length()));
                decryptedText = decryptedText.substring(decryptedWord.length());
                decryptedWords.add(decryptedWord);
            }
        }
        return new DecryptionResult(key, decryptedWords, isComplete, QuantitiesCalculator.calculateChiCriterion(QuantitiesCalculator.calculateEmpiricalQuantities(decryptedText), nGrammsQuantities), decryptedText);
    }

}
