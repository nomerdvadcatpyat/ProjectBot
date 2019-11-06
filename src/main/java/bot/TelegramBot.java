package bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.logging.Logger;

public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());
    private Model model = new Model();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try{
            var bot = new TelegramBot();
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e){
            logger.info(e.getMessage());
        }
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()){
            Message message = update.getCallbackQuery().getMessage();
            String data = update.getCallbackQuery().getData();
            if (!data.isEmpty()){
                MenuState lastState = model.getMenuState();
                model.updateMenuState(data);
                if(lastState != model.getMenuState())
                    editMessageText(message, model.getStateInfoText());
                String answer;
                try {
                    answer = model.getStateAnswer(data);
                }
                catch (Exception e){
                    answer = null;
                    switch (model.getMenuState()) {
                        case PHOTO_GETTER:
                            editMessageText(message, "Image not found");
                            break;
                        default:
                            logger.info("default");
                            editMessageText(message, "Error");
                    }
                }
                if(!answer.isEmpty()) {
                    switch (model.getMenuState()) {
                        case MAIN_MENU:
                            logger.info("sendAnim");
                            sendAnimationFromDisk(message, answer);
                            break;

                        case PHOTO_GETTER:
                            sendPhotoByURL(message, answer);
                            break;

                        default:
                            logger.info("default");
                            sendMessage(message, answer);
                    }
                }
                logger.info(model.getMenuState().toString());
            }
        }
        else if (update.hasMessage()){
            logger.info("Чат id - " + update.getMessage().getChatId().toString());
            Message message = update.getMessage();

            if (message != null && message.hasText()) {
                String messageText = message.getText();
                MenuState lastState = model.getMenuState();
                model.updateMenuState(messageText);
                if(lastState != model.getMenuState())
                    sendMessage(message, model.getStateInfoText());
                String answer;
                try {
                    answer = model.getStateAnswer(messageText);
                }
                catch (Exception e){
                    answer = null;
                    switch (model.getMenuState()) {
                        case PHOTO_GETTER:
                            sendMessage(message, "Image not found");
                            break;
                        default:
                            logger.info("default");
                            sendMessage(message, "Error");
                    }
                }
                if(!answer.isEmpty()) {
                    switch (model.getMenuState()) {
                        case MAIN_MENU:
                            logger.info("sendAnim");
                            sendAnimationFromDisk(message, answer);
                            break;

                        case PHOTO_GETTER:
                            sendPhotoByURL(message, answer);
                            break;

                        default:
                            logger.info("default");
                            sendMessage(message, answer);
                    }
                }
                logger.info(model.getMenuState().toString());
            }
        }


    }

    private void sendMessage(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        InlineKeyboardMarkup keyboard = model.getKeyboard();
        if (keyboard != null)
            sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            logger.info(e.getMessage());
        }
    }

    private void editMessageText(Message message, String text){
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableMarkdown(true);
        editMessageText.setChatId(message.getChatId());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setText(text);
        InlineKeyboardMarkup keyboard = model.getKeyboard();
        if (keyboard != null)
            editMessageText.setReplyMarkup(keyboard);
        try {
            execute(editMessageText);
        }catch(TelegramApiException e){
            logger.info(e.getMessage());
        }
    }

    private void sendPhotoByURL(Message message, String url){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(url);
        sendPhoto.setChatId(message.getChatId().toString());
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            logger.info(e.getMessage());
        }
    }

    private void sendAnimationByURL(Message message, String url){
        SendAnimation sendAnimation = new SendAnimation();
        sendAnimation.setAnimation(url);
        sendAnimation(message, sendAnimation);
    }

    private void sendAnimationFromDisk(Message message, String path){
        SendAnimation sendAnimation = new SendAnimation();
        sendAnimation.setAnimation(new File(path));
        sendAnimation(message, sendAnimation);
    }

    private void sendAnimation(Message message, SendAnimation sendAnimation){
        sendAnimation.setChatId(message.getChatId().toString());
        try {
            execute(sendAnimation);
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
