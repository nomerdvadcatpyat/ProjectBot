package bot;

import java.util.*;

public class Program {
    public static void main(String[] args) {
        ConsoleBot bot = new ConsoleBot();
        bot.initialize();
        Scanner sc = new Scanner(System.in);
        if(sc.nextLine().equals("!старт"))
            bot.initialize();
    }
}