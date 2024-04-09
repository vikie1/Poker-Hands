package come.github.vikie1;

import java.io.*;

public class Play {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    private void readPokerFile() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream("poker.txt")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        }
    }
}