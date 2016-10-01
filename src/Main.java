import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> vocabulary = Files.lines(Paths.get("resources/vocabulary.txt")).collect(toList());

        Decoder decoder = new Decoder(vocabulary, "abcdefghijklmnopqrstuvwxyz");
        System.out.println("result");
        DecryptionResult result = decoder.decode(("EFFPQLMEHEMVPCPYFLMVHQLUHYTCETHQEKLPVMMVHQLUWEOLFPQLIVDLWLULMVHQLUCYAUHUYDUEOSQY" +
                "ATFFVSMUVOVWEPLPQVSPCHLYGETDYUVPQOGUYOOYWYETHQEKLPVMYWLSASVWDEWCPLSPYGDYYFWLSSYGGVPCYAEULMYO" +
                "GYUPEKTLBVPQCYAOECASLVWFLRYGMYVWMVFLWMLNESVSNVLREOVWEPVYWSPEPVSPVMETPLSPSYUBQEPLILUOLPQYFCYAGLLT" +
                "BVTTSQYBPQLKLSPULSATPPUCPYPQVWNYGBQEPBVTTQEHHLWVGPQLNLCMYWPEVWSSEOLMQEUEMPLUSEPFVGGLULWPHYSVPVYWSB" +
                "VTTCYAUHUYDUEOSPVTTKLQEILMYUULMPTC").toLowerCase());
        PrintWriter printWriter = new PrintWriter(new File("res.txt"));
        printWriter.println(result.getDecryptedWords().stream().collect(Collectors.joining()));
        System.out.println("Done.");
    }
}