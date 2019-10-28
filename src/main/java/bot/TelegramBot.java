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

        if (message != null && message.hasText()) {
            String messageText = message.getText();
            MenuState lastState = model.getMenuState();
            model.updateState(messageText);
            if(lastState != model.getMenuState())
                sendMessage(message,model.getStateHelloMessage());

            logger.info(model.getMenuState().toString());


            switch (model.getMenuState()) {
                case PhotoGetter:
                    if(messageText.equals("PhotoGetter"))
                        break;
                    try {
                        sendPhoto(message, PhotoGetter.getPhotoURL(messageText));
                    } catch (Exception e) {
                        sendMessage(message, "Image not found");
                    }
                    break;

                case CitiesGame:
                    if (messageText.equals("Cities")) {
                        citiesGame = new CitiesGame();
                        break;
                    }
                    sendMessage(message, citiesGame.getAnswer(messageText));
            }
        }
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
        return "KavoIShoBot";
    }

    @Override
    public String getBotToken() {
        return "811627871:AAGPmmHpbufDY-2pd8YhhrnWiWJI_EGDUvo";
    }
}
