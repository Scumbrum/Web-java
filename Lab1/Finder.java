import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Finder {

    String workDirectory;
    String outputFile;
    String data;

    public Finder(String inputFile, String outputFile) {
        this.workDirectory = inputFile;

        this.outputFile = outputFile;
    }

    public void findStrings(String key) throws InterruptedException, IOException {

        Map<String, String> fileString = getStringsMap(key);

        this.data = writeStrings(fileString);

    }

    private Map<String, String> getStringsMap(String key) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(100);
        DirectoryThread thread = new DirectoryThread(workDirectory, executor, key);

        thread.start();
        thread.join();

        executor.shutdown();

        return  thread.getFileContent();
    }

    private String writeStrings(Map<String, String> fileString) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        StringBuffer data = new StringBuffer();

        System.out.println("Writing founded data into " + outputFile);

        try(writer) {
            for(String key: fileString.keySet()) {

                writer.write(key + ":");
                writer.newLine();
                writer.write("    " + fileString.get(key));
                writer.newLine();

                data.append(key+ ":\n" + "    " + fileString.get(key));
                data.append("\n");
            }
        }
        return data.toString();
    }

    public String getData() {
        return data;
    }
}
