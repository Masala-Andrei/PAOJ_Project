package Services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static String filepath = "src/main/java/AuditLog.csv";

    public void logAction(String action){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        try (FileWriter fileWriter = new FileWriter(filepath, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println(formattedDate + ":" + action);

        } catch (IOException e) {
            System.err.println("Error writing to audit file: " + e.getMessage());
        }
    }
}
