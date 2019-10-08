package bot;

import bot.games.CitiesGame;

public interface Bot {
    default void initialize(){
        this.printAnswer("Привет,я (еще не(а может и совсем не)) универсальный бот!\n" +
                "Выбери категорию :\n 1.Игры \n 2.Еще ничего \n 3.Тоже ничего");
        if(getCommand().equals("1"))
            this.printAnswer("Вы выбрали категорию игры. В какую игру вы хотите сыграть?\n" +
                    "1. Города\n 2. Ничего.");
        if(getCommand().equals("1")){
            CitiesGame game = new CitiesGame(this);
            game.run();
        }
    };
    public void stop();
    public String getCommand();
    public void printAnswer(String answer);
}


