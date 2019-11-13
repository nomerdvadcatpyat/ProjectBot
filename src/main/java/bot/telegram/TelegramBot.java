package bot.telegram;

import bot.model.MenuState;
import bot.model.Model;
import bot.model.StateData;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TelegramBot extends TelegramLongPollingBot {

    public TelegramBot() {
        setupInlineKeyboards();
    }

    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());
    private Model model = new Model();
    private HashMap<MenuState, StateData> statesInfo = model.getStatesInfo();
    private HashMap<Long, Model> chatIdModel = new HashMap<>();
    private boolean isKeyboardEnabled = false;

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
        if (update.hasCallbackQuery() || update.hasMessage()){
            Long chatId = null;
            Message message = null;
            String data = null;
            if(update.hasCallbackQuery()){
                isKeyboardEnabled = true;
                chatId = update.getCallbackQuery().getMessage().getChatId();
                message = update.getCallbackQuery().getMessage();
                data = update.getCallbackQuery().getData();
            }
            if(update.hasMessage()){
                isKeyboardEnabled = false;
                chatId = update.getMessage().getChatId();
                message = update.getMessage();
                data = message.getText();
            }
            logger.info("chat id " + chatId);
            if(!chatIdModel.containsKey(chatId))
                chatIdModel.put(chatId, new Model());
            model = chatIdModel.get(chatId);
            if (data != null && !data.isEmpty()){
                MenuState lastState = model.getMenuState();
                model.updateMenuState(data);
                if(lastState != model.getMenuState())
                    sendMessage(message, model.getStateInfoText());
                String answer;
                try {
                    answer = model.getStateAnswer(data);
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
                if(answer != null && !answer.isEmpty()) {
                    switch (model.getMenuState()) {
/*Шрек                        case MAIN_MENU:
                            logger.info("sendAnim");
                            sendAnimationFromDisk(message, answer);
                            break;*/

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
        if(isKeyboardEnabled){
            editMessageText(message, text);
            return;
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        InlineKeyboardMarkup keyboard = getKeyboard();
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
        InlineKeyboardMarkup keyboard = getKeyboard();
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


/*Шрек    private void sendAnimationFromDisk(Message message, String path){
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
    }*/

    private void setupInlineKeyboards(){
        for (Map.Entry<MenuState, StateData> entry : statesInfo.entrySet()){
            StateData data = entry.getValue();
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            List<InlineKeyboardButton> buttonsRow1 = new ArrayList<>();
            List<InlineKeyboardButton> buttonsRow2 = new ArrayList<>();
            if (data.getChildren() != null) {
                List<MenuState> children = data.getChildren();
                for (MenuState child : children) {
                    String childName = statesInfo.get(child).getName();
                    InlineKeyboardButton button = new InlineKeyboardButton().setText(childName).setCallbackData(childName);
                    buttonsRow1.add(button);
                }
            }
            if (data.getParent() != null) {
                String parentName = statesInfo.get(data.getParent()).getName();
                buttonsRow2.add(new InlineKeyboardButton().setText("< Back").setCallbackData(parentName));
                buttonsRow2.add(new InlineKeyboardButton().setText("Main").setCallbackData(MenuState.MAIN_MENU.getName()));
            }
            buttons.add(buttonsRow1);
            buttons.add(buttonsRow2);
            InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
            markupKeyboard.setKeyboard(buttons);
            data.keyboard = new InlineKeyboardMarkup();
            data.keyboard.setKeyboard(buttons);
        }
    }

    private InlineKeyboardMarkup getKeyboard(){
        return statesInfo.get(model.getMenuState()).keyboard;
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
