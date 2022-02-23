import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.*;

public class DirectoryThread extends Thread {

    private Map<String, String> fileContent = new ConcurrentHashMap<>();
    private String fileName;
    private ExecutorService executor;
    private String key;

    public DirectoryThread(String fileName, ExecutorService executor, String key) {
        this.fileName = fileName;
        this.executor = executor;
        this.key = key;
    }

    public void setFileContent(Map fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public void run() {

        File currentFile = new File(this.fileName);
        File[] childrenFiles = currentFile.listFiles();
        ArrayList<Future> futures = new ArrayList<>();
        if(!currentFile.exists()) {
            System.out.println("ss");
            fileContent = null;
            return;
        }

        for(File child: childrenFiles) {

            String nextFileName = fileName + File.separator + child.getName();

            if(child.isDirectory()) {
                System.out.println("Go into directory " + nextFileName);
                goIntoDirectory(nextFileName, futures);

            } else {
                System.out.println("Read file " + nextFileName);
                tryExtract(nextFileName);

            }
        }

        waitingFor(futures);

    }

    private void waitingFor(ArrayList<Future> futures) {
        for(Future future: futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void goIntoDirectory(String directoryName, ArrayList<Future> futures) {
        DirectoryThread caller = new DirectoryThread(directoryName, executor, key);
        caller.setFileContent(fileContent);
        futures.add(executor.submit(caller));
    }

    private void tryExtract(String fileName) {
        try {
            FileExtractor extractor = new FileExtractor(fileName);
            String stringWithKey = extractor.getStringWithKey(key);
            if (stringWithKey != null) {
                fileContent.put(fileName, stringWithKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getFileContent() {
        return fileContent;
    }
}
