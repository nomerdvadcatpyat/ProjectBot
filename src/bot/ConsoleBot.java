package bot;

import java.io.*;
import java.util.*;

public class ConsoleBot implements Bot{
    private Scanner sc = new Scanner(System.in);

    public void stop() {
        System.out.println("конец.");
    }

    public String getCommand() {
        return sc.nextLine();
    }

    public void printAnswer(String answer) {
        System.out.println(answer);
    }
}