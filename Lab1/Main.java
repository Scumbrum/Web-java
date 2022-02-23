import java.io.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter work directory - >");
        String workDirectory = scanner.nextLine();
        System.out.println("Enter output file - >");
        String outputDirectory = scanner.nextLine();
        System.out.println("Enter key word - >");
        String key = scanner.nextLine();
        Finder finder = new Finder(workDirectory, outputDirectory);
        finder.findStrings(key);
        System.out.println("Output:");
        System.out.println(finder.getData());
    }

}
