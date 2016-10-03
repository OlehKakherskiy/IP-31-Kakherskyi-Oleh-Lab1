import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> vocabulary = Files.lines(Paths.get("resources/vocabulary.txt")).sorted(Comparator.comparing(String::length).reversed()).collect(toList());

        DecryptionAnalyzer.nGrammsQuantities = QuantitiesCalculator.getTheoreticalQuantities("resources/3-gramms.txt");
//        DecryptionAnalyzer.nGrammsQuantities.putAll(QuantitiesCalculator.getTheoreticalQuantities("resources/1-gramms.txt"));
        DecryptionAnalyzer.nGrammsQuantities.putAll(QuantitiesCalculator.getTheoreticalQuantities("resources/2-gramms.txt"));
//        DecryptionAnalyzer.nGrammsQuantities.putAll(QuantitiesCalculator.getTheoreticalQuantities("resources/4-gramms.txt"));
        Decoder decoder = new Decoder(vocabulary, "abcdefghijklmnopqrstuvwxyz");
        System.out.println("result");
        DecryptionResult result = decoder.decode(("EFFPQLMEHEMVPCPYFLMVHQLUHYTCETHQEKLPVMMVHQLUWEOLFPQLIVDLWLULMVHQLUCYAUHUYDUEOSQY" +
                "ATFFVSMUVOVWEPLPQVSPCHLYGETDYUVPQOGUYOOYWYETHQEKLPVMYWLSASVWDEWCPLSPYGDYYFWLSSYGGVPCYAEULMYO" +
                "GYUPEKTLBVPQCYAOECASLVWFLRYGMYVWMVFLWMLNESVSNVLREOVWEPVYWSPEPVSPVMETPLSPSYUBQEPLILUOLPQYFCYAGLLT" +
                "BVTTSQYBPQLKLSPULSATPPUCPYPQVWNYGBQEPBVTTQEHHLWVGPQLNLCMYWPEVWSSEOLMQEUEMPLUSEPFVGGLULWPHYSVPVYWSB" +
                "VTTCYAUHUYDUEOSPVTTKLQEILMYUULMPTC").toLowerCase());
//        DecryptionResult result = decoder.decode(("LSSYEWRPLRFHZZFXHUZWMGUWVOSWKWFDWMRHZPPDVZVTBPAIEMSARWYSHNRBPZQCUSYCC" +
//                "DARXWCXELAQSWGSJPROECEOHQRLELWWKSFEFJAQWWCXXLXZFGSZGHSAFLLZLCUSPWSOGLKTRNKCXIQUNGKYIVOSJIHFBCFPLOBIJIHQRF" +
//                "WTGGNLDLFPRDGILFUWUSLWFLHLFCBXPKCVRLGWRGEIJYWCHFWIKRZVXZIAKTDZKNEDGEGGYCULHNQCFIHXCLGQLRGWLIOUFCZLGJFCEPL" +
//                "YNDWCLPYUWOLCBLARLOPCEIHQRFAGVCKGAOUGJVSELHBIPCFNKGWEFLUWFRTXELADWWKHZPPXKSMELDCLXCCWGHZPFRTPJHOUYLXCCVKW" +
//                "FDWMRLZSSARGPLUFUWLPLLBZGCSMYSLESSNZDIMXHBWPRRFLAYGRQSPEVRFLDPHCRFPJWDKASJQXAQWLZGSFGXLNFSJJCMRLOSOCJOKTBB" +
//                "JRWMICCZWLGNKRGIBXGTGCUNGLLZDXFHGYZRXSPEVNKGWNFNGLXZVAOMLPQXQSPKFRZHWYLMACFPLBVRWICOGVWIFNJOJOCWPSFZIGTSLI" +
//                "WCVHPXOHKQGXSGVBPSOCQMKZANKHAXSYYOLPFGNBVIHQVGADHQRLGYZHVYQIWLNBTPLBHFWHVXJOKEVNSWJDHXASLZUNGLLSWBKAWDGJTS" +
//                "PCWPUHBFGCUFAEWWTAWEVNCZSTBCRLLIRXRGFZHLBIFESDSSHELOBFLSOCKMGFLJESPQFNRHGOCFLOLPJNELQZIGJWKSWDLCDIKWAHPQWW" +
//                "QOFJHQVBYIOCRVWIRNFWYYORRRPAZJPSPEVNASAEVNEMGFLJESPNCCFSSCQQVBYIKNFZJZUBBAWZBNKSDDSPBHPEUCTSHCWLRLXTFBGLHY" +
//                "SJFSPISJISLSSGRAAEMURHLTFDCLLSSARLKZSERFQZBNKKGFZMXBGHPOBFKFFNKHZPMPHSKDSMKHZTGXASPECXYOLPWOLCMCDABUJLAGQS" +
//                "UTDQRFWOHQVGSFHXZOLTQJYZQIHQRBPTLBUCMWRGPCFRFJGIDLHNJWLSLCUSELLRZOEDQXESPZBNPC" +
//                "MWRGNQZTSERKAEVGGVADLUNPOZTTLCMCLFBFCIQJAADVSGNUJPOCKRWLZGNHPEHNKBWIIGCIRKZNKRSJOUBILEVRFLSIZRGHDILUNHWDEQNBCJCDKL").toLowerCase());
        PrintWriter printWriter = new PrintWriter(new File("res.txt"));
        printWriter.println(result.getDecryptedWords().stream().collect(Collectors.joining()));
        System.out.println("Done.");
    }
}