package bot;

import bot.games.CitiesGame;

public interface IBot {

    default void initialize(){
        while (!isStop()){
            printAnswer("Привет,я (еще не(а может и совсем не)) универсальный бот!\n" +
                    "Выбери категорию :\n 1.Игры \n 2.Еще ничего \n 3.Тоже ничего");

            switch (getCommand().toLowerCase()) {
                case "1", "игры": {
                    printAnswer("Вы выбрали категорию игры. В какую игру вы хотите сыграть?\n" +
                            " 1. Города\n 2. Ничего.");
                    break;
                }

                case "2", "3", "ничего": {
                    printAnswer("Вы выбрали категорию НИЧЕГО.");
                    continue;
                }

                default: printAnswer("Я не знаю такой команды.");
            }
            if(isStop()) continue;

            switch (getCommand().toLowerCase()) {
                case "1", "города": {
                    CitiesGame game = new CitiesGame(this);
                    game.run();
                    break;
                }

                case "2", "3", "ничего": {
                    printAnswer("Вы выбрали категорию НИЧЕГО.");
                    continue;
                }

                default: printAnswer("Я не знаю такой команды.");
            }
            if(isStop()) continue;
        }
    }

    public boolean isStop();
    public String getCommand();
    public void printAnswer(String answer);
    public void printHelp();
}


