import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class FileExtractor {
    private String fileName;
    private String data = null;

    public FileExtractor(String fileName) {
        this.fileName = fileName;
    }

    public String getStringWithKey(String key) throws IOException {
        String suitString;
        if(data == null) {
            suitString = getStringInRunTime(key);
        } else {
            suitString = getStringFromData(key);
        }
        return suitString;
    }

    private String getStringFromData(String key) {
        String[] words = data.split("\n");
        Optional<String> suit = Stream.of(words)
                .filter(line -> hasKey(line, key))
                .findFirst();
        if(suit.isEmpty()) {
            return null;
        }
        return suit.get();
    }

    private String getStringInRunTime(String key) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String suitLine = null;
        try(reader) {
            String line = reader.readLine();

            while (line != null) {
                if(hasKey(line, key)) {
                    suitLine = line;
                    break;
                }
                line = reader.readLine();
            }
        }
        return suitLine;
    }

    private boolean hasKey(String line, String key) {
        String[] words = line.split(" ");
        Optional<String> keyWord = Stream.of(words)
                .filter(word -> word.equals(key))
                .findFirst();
        return !keyWord.isEmpty();
    }
}
