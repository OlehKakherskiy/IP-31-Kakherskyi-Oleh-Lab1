import java.util.List;
import java.util.Map;

/**
 * Created by Oleg on 30.09.2016.
 */
public class DecryptionResult {

    private Map<String, String> key;

    private List<String> decryptedWords;

    private boolean isRightDecryption;

    private double chiCriterion;

    private String decryptedText;

    public DecryptionResult(Map<String, String> key, List<String> decryptedWords, boolean isRightDecryption, double chiCriterion, String decryptedText) {
        this.key = key;
        this.decryptedWords = decryptedWords;
        this.isRightDecryption = isRightDecryption;
        this.chiCriterion = chiCriterion;
        this.decryptedText = decryptedText;
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

    public double getChiCriterion() {
        return chiCriterion;
    }

    @Override
    public String toString() {
        return "DecryptionResult{" +
                "key=" + key +
                ", decryptedWords=" + decryptedWords +
                ", isRightDecryption=" + isRightDecryption +
                ", chiCriterion=" + chiCriterion +
                ", decryptedText='" + decryptedText + '\'' +
                '}';
    }
}