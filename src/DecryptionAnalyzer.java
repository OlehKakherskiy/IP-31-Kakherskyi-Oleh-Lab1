import java.util.Comparator;
import java.util.List;

/**
 * Created by Oleg on 05.10.2016.
 */
public class DecryptionAnalyzer {

    public boolean analyze(DecryptionResult decryptionResult, List<String> vocabulary) {
        String decodedText = decryptionResult.getDecryptedText();
        String decodedWord = null;
        while (decodedText.length() > 0) {
            decodedWord = vocabulary.stream().filter(decodedText::startsWith).max(Comparator.comparing(String::length)).orElse("");
            if (decodedWord.isEmpty()) {
                return false;
            } else {
                decodedText = decodedWord.substring(decodedWord.length());
            }
        }
        return true;
    }
}
