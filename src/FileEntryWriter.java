import java.io.*;
import java.util.Scanner;

/**
 * Created by Snoob on 4/12/2016.
 */
public class FileEntryWriter {
    String filename = "";

    public FileEntryWriter(String filename) throws IOException {
        this.filename = filename;
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public synchronized String get() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String tempFile = "";
        String result = null;
        boolean FirstLine = true;
        while (scanner.hasNext()) {
            String next = scanner.nextLine();
            if (!FirstLine) {
                tempFile += next;
                tempFile += "\n";
            }
            if (result == null) result = next;
            FirstLine = false;
        }
        scanner.close();
        PrintWriter out = null;
        try {
            out = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.print(tempFile);
        out.close();
        return result;
    }

    public synchronized void insert(String entry) throws IOException {
        Writer fileWriter = new FileWriter(filename, true);
        fileWriter.write(entry);
        fileWriter.write("\n");
        fileWriter.close();
    }
}
