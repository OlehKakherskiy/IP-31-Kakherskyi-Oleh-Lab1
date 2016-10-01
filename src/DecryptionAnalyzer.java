import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleg on 01.10.2016.
 */
public class DecryptionAnalyzer {

    public static DecryptionResult isRightDecryption(String decryptedText, Map<String, String> key, List<String> vocabulary) {
        System.out.println("decryptedText = " + decryptedText);
        List<String> decryptedWords = new ArrayList<>();
        while (!decryptedText.isEmpty()) {
            String decryptedWord = vocabulary.stream().unordered().filter(decryptedText::startsWith).findFirst().orElse("");
            System.out.println("decryptedWord = " + decryptedWord);
            if (decryptedWord.isEmpty()) {
                return new DecryptionResult(key, decryptedWords, false);
            } else {
                decryptedText = decryptedText.substring(decryptedWord.length());
                decryptedWords.add(decryptedWord);
            }
        }
        return new DecryptionResult(key, decryptedWords, true);
    }

}
