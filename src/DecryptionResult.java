/**
 * Created by Oleg on 30.09.2016.
 */
public class DecryptionResult {

    private String key;

    private double criterion;

    private String decryptedText;

    public String getKey() {
        return key;
    }

    public double getCriterion() {
        return criterion;
    }

    public String getDecryptedText() {
        return decryptedText;
    }

    public DecryptionResult(String key, double criterion, String decryptedText) {
        this.key = key;
        this.criterion = criterion;
        this.decryptedText = decryptedText;
    }

    @Override
    public String toString() {
        return "DecryptionResult{" + "key=" + key + ", criterion=" + criterion + ", decryptedText='" + decryptedText + '\'' + '}';
    }
}