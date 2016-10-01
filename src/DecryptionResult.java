import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleg on 30.09.2016.
 */
public class DecryptionResult {

    private Map<String, String> key;

    private List<String> decryptedWords;

    private boolean isRightDecryption;

    public DecryptionResult(Map<String, String> key, List<String> decryptedWords, boolean isRightDecryption) {
        this.key = key;
        this.decryptedWords = decryptedWords;
        this.isRightDecryption = isRightDecryption;
    }

    public Map<String, String> getKey() {
        return key;
    }

    public List<String> getDecryptedWords() {
        return decryptedWords;
    }

    public boolean isRightDecryption() {
        return isRightDecryption;
    }

    public long decryptedLetters() {
        return decryptedWords.stream().map(s -> s.split("")).flatMap(Arrays::stream).distinct().count();
    }

    @Override
    public String toString() {
        return "DecryptionResult{" +
                "key=" + key +
                ", decryptedWords=" + decryptedWords +
                ", isRightDecryption=" + isRightDecryption +
                '}';
    }
}