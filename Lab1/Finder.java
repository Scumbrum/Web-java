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

    public void findStrings(String key) throws InterruptedException {

        if(workDirectory =="" || outputFile == "") {
            throw new IllegalArgumentException("Illegal path");
        }
        if(key == "" | isNumber(key)) {
            throw  new IllegalArgumentException("Invalid key");
        }

        try {
            Map<String, String> fileString = getStringsMap(key);
            if(fileString == null) {
                throw new IllegalArgumentException("Non exists directory");
            }
            if (fileString.size()==0) {
                this.data = writeStrings("Can't find key");
            } else {
                this.data = writeStrings(fileString);
            }
        } catch (IOException e) {
            System.out.println("Bad output");
        }

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

    private String writeStrings(String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        System.out.println("Writing founded data into " + outputFile);

        try(writer) {
           writer.write(data);
        }

        return data;
    }

    public String getData() {
        return data;
    }

    private boolean isNumber(String line) {
        try {
            Integer.parseInt(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
