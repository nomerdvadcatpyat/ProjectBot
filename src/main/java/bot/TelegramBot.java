package bot;

import bot.games.cities.CitiesGame;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());
    private Model model = new Model();
    private CitiesGame citiesGame;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try{
            telegramBotsApi.registerBot(new TelegramBot());

        } catch (TelegramApiException e){
            logger.info(e.getMessage());
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        model.updateModel(message.getText());
        logger.info(model.getModelState().toString());

        if (message != null && message.hasText()) {
            switch (model.getModelState()) {
                case MainMenu:
                    sendMessage(message, "Ты в MainMenu. Доступные опции: \n1)Tools \n2)Games");
                    break;

                case ToolsMenu:
                    sendMessage(message, "Ты в ToolsMenu. Доступные опции: \n1)PhotoGetter");
                    break;

                case GamesMenu:
                    sendMessage(message, "Ты в GamesMenu. Доступные игры: \n1)Cities");
                    break;

                case PhotoGetter:
                    if(message.getText().equals("PhotoGetter")) {
                        sendMessage(message,"Ты в PhotoGetter.");
                        break;
                    }
                    try {
                        sendPhoto(message, PhotoGetter.getPhotoURL(message.getText()));
                    } catch (Exception e) {
                        sendMessage(message, "Image not found");
                    }
                    break;

                case CitiesGame:
                    if(message.getText().equals("Cities")) {
                        sendMessage(message,"Ты в CitiesGame. Назови любой город: ");
                        citiesGame = new CitiesGame();
                        break;
                    }
                    sendMessage(message,citiesGame.getAnswer(message.getText()));
                    break;
            }
        }
            /*switch (message.getText()){
                case "/start":
                    sendMessage(message,"Hello");
                    break;
                case"/photo":
                    sendPhoto(message, "https://pixabay.com/get/55e2dc414351ae14f6da8c7dda79367a153dd9e451516c4870287ad3924bc650b0_1280.jpg");
                    break;
                default:
                    try {
                        System.out.println(message.getText());
                        sendPhoto(message, PhotoGetter.getPhotoURL(message.getText()));
                    } catch (Exception e) {
                        sendMessage(message, "Image not found");
                    }
            }*/
    }

    public void sendMessage(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            logger.info(e.getMessage());
        }
    }

    public void sendPhoto(Message message, String url){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(url);
        sendPhoto.setChatId(message.getChatId().toString());
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            logger.info(e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return "OOPContentBot";
    }

    @Override
    public String getBotToken() {
        return "";
    }
}
