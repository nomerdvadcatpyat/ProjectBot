package bot;

import bot.model.MenuState;
import bot.model.Model;
import bot.model.StateData;
import bot.tools.locator.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class TelegramBot extends TelegramLongPollingBot {

    public TelegramBot() {
        setupInlineKeyboards();
        statesInfo.get(MenuState.LOCATOR).keyboard = getLocationKeyboard();
        statesInfo.get(MenuState.MOVIE_RANDOMIZER).keyboard = getMovieRandomizerKeyboard();
        statesInfo.get(MenuState.MINESWEEPER).keyboard = getMinesweeperKeyboard();
        statesInfo.get(MenuState.KUDA_GO).keyboard = getKudaGoCitiesKeyboard();
    }

    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());
    private Model model = new Model();
    private HashMap<MenuState, StateData> statesInfo = Model.statesInfo;
    private HashMap<Long, Model> chatIdModel = new HashMap<>();
    private static final String PORT = System.getenv("PORT");

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try{
            TelegramBot bot = new TelegramBot();
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e){
            logger.info(e.getMessage());
        }
        try (ServerSocket serverSocket = new ServerSocket(Integer.valueOf(PORT))) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery() || update.hasMessage()) {
            Long chatId = null;
            Message message = null;
            String data = null;
            BiConsumer<Message, String> deliveryman = null;
            if(update.hasCallbackQuery()){
                chatId = update.getCallbackQuery().getMessage().getChatId();
                message = update.getCallbackQuery().getMessage();
                data = update.getCallbackQuery().getData();
                deliveryman = (m, t) -> editMessageText(m, t);
                if (data.equals(MenuState.LOCATOR.getName()) || data.equals(MenuState.MOVIE_RANDOMIZER.getName())
                        || data.equals(MenuState.KUDA_GO.getName()))
                    deliveryman = (m, t) -> sendMessage(m, t);
            }
            if(update.hasMessage()) {
                chatId = update.getMessage().getChatId();
                message = update.getMessage();
                data = message.getText();
                deliveryman = (m, t) -> sendMessage(m, t);
            }
            if (message.hasLocation()){
                logger.info("Location received");
                org.telegram.telegrambots.meta.api.objects.Location location = message.getLocation();
                String locationUpdatedAnswer = model.locator.updateLocation(new Location(location.getLatitude(), location.getLongitude()));
                deliveryman.accept(message, locationUpdatedAnswer);
            }
            logger.info("chat id " + chatId);
            if(!chatIdModel.containsKey(chatId))
                chatIdModel.put(chatId, new Model());
            model = chatIdModel.get(chatId);
            if (data != null && !data.isEmpty()) {
                MenuState lastState = model.getMenuState();
                model.updateMenuState(data);
                if(lastState != model.getMenuState()) {
                    String infoText = model.getStateInfoText();
                    if (lastState != null && model.isStateWithReplyKeyboard(lastState))
                        sendMessageWithDeletingReplyKeyboard(message, infoText);
                    else
                        deliveryman.accept(message, infoText);
                }
                String answer;
                try {
                    answer = model.getStateAnswer(data);
                }
                catch (Exception e){
                    answer = null;
                    switch (model.getMenuState()) {
                        case PHOTO_GETTER:
                            deliveryman.accept(message, "Image not found");
                            break;
                        default:
                            logger.info(e.getMessage());
                            deliveryman.accept(message, "Error");
                    }
                }
                if(answer != null && !answer.isEmpty()) {
                    logger.info("answer " + answer);
                    switch (model.getMenuState()) {
            /*Шрек                        case MAIN_MENU:
                logger.info("sendAnim");
                sendAnimationFromDisk(message, answer);
                break;*/

                        case PHOTO_GETTER:
                            sendPhotoByURL(message, answer);  //"https://cdn.pixabay.com/photo/2017/09/14/11/07/water-2748640_1280.png"
                            break;
                        case KUDA_GO:
                            if(answer.equals("/new")) {
                                statesInfo.get(MenuState.KUDA_GO).keyboard = getKudaGoCitiesKeyboard();
                                answer = "Выберите город.";
                            }
                            if(model.isCitySelectedInKudaGo()) statesInfo.get(MenuState.KUDA_GO).keyboard = getKudaGoKeyboard();
                        case MOVIE_RANDOMIZER:
                        case LOCATOR:
                            logger.info("ANSWER " + answer);
                            if (answer.contains("{")) {
                                logger.info("contains");
                                JSONObject jsonAnswer = new JSONObject(answer);
                                String messageText = jsonAnswer.getString("message");
                                String url = jsonAnswer.getString("url");
                                logger.info("MESSAGE" + messageText + "\\n URL: " + url);
                                sendPhotoByURL(message, url);
                                sendMessage(message, messageText);
                            }
                            else
                                sendMessage(message, answer);
                            break;
                        case MINESWEEPER:
                            InlineKeyboardMarkup keyboard = (InlineKeyboardMarkup) statesInfo.get(MenuState.MINESWEEPER).keyboard;
                            if(answer.contains("{")) {
                                JSONObject obj = new JSONObject(answer);
                                List<List<InlineKeyboardButton>> buttons = keyboard.getKeyboard();
                                if (obj.has("openCells")) {
                                    JSONArray jsonArray = obj.getJSONArray("openCells");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject point = jsonArray.getJSONObject(i);
                                        int x = point.getInt("x");
                                        int y = point.getInt("y");
                                        buttons.get(x).get(y).setText(" " + new String(Character.toChars(model.getCellEmojiCode(x, y))) + " ");
                                    }
                                }
                                if (obj.has("markedCell")) {
                                    int x = obj.getJSONObject("markedCell").getInt("x");
                                    int y = obj.getJSONObject("markedCell").getInt("y");
                                    buttons.get(x).get(y).setText(" " + new String(Character.toChars(model.getCellEmojiCode(x, y))) + " ");
                                }
                                editMessageText(message, obj.getString("message"));
                                break;
                            }
                            if (answer.equals("/newGame")) {
                                statesInfo.get(MenuState.MINESWEEPER).keyboard = getMinesweeperKeyboard();
                                editMessageText(message, "Новая игра");
                                break;
                            }
                            InlineKeyboardButton flagButton = keyboard.getKeyboard().get(keyboard.getKeyboard().size() - 2).get(0);
                            if (answer.equals("/flag0"))
                                flagButton.setText("Открыть");
                            if (answer.equals("/flag1"))
                                flagButton.setText("Флаг    " + new String(Character.toChars(0x1F6A9)));
                            editMessageText(message, model.getMinesweeperInfo());
                            break;
                        default:
                            logger.info("default");
                            deliveryman.accept(message, answer);
                    }
                }
                logger.info(model.getMenuState().toString());
            }
        }
    }

    private void sendMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        ReplyKeyboard keyboard = getKeyboard();
        if (keyboard != null && (model.getMenuState() != MenuState.LOCATOR ||
                (model.locator != null && !model.locator.isLocationInitiallyUpdated())))
            sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            logger.info(e.getMessage());
        }
    }

    private void editMessageText(Message message, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableMarkdown(true);
        editMessageText.setChatId(message.getChatId());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setText(text);
        ReplyKeyboard keyboard = getKeyboard();
        if (keyboard != null)
            editMessageText.setReplyMarkup((InlineKeyboardMarkup) keyboard);
        try {
            execute(editMessageText);
        }catch(TelegramApiException e){
            logger.info(e.getLocalizedMessage());
        }
    }

    private void sendMessageWithDeletingReplyKeyboard(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        Message newMessage = null;
        try {
             newMessage = execute(sendMessage);
        }catch (TelegramApiException e){
            logger.info(e.getMessage());
        }
        deleteMessage(newMessage);
        sendMessage(newMessage, text);
    }

    private void deleteMessage(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId());
        deleteMessage.setMessageId(message.getMessageId());
        try{
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            logger.info(e.getMessage());
        }
    }

    private void sendPhotoByURL(Message message, String url) {
        System.out.println("url in sendPhoto " + url);
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

    private void setupInlineKeyboards() {
        for (Map.Entry<MenuState, StateData> entry : statesInfo.entrySet()) {
            StateData data = entry.getValue();
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            List<InlineKeyboardButton> buttonsRow1 = new ArrayList<>();
            List<InlineKeyboardButton> buttonsRow2 = new ArrayList<>();
            if (data.getSubmenus() != null) {
                List<MenuState> submenus = data.getSubmenus();
                for (MenuState submenu : submenus) {
                    String childName = statesInfo.get(submenu).getName();
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
            ((InlineKeyboardMarkup) data.keyboard).setKeyboard(buttons);
        }
    }

    private ReplyKeyboardMarkup getLocationKeyboard() {
        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton locationButton = new KeyboardButton("Отправить геопозицию");
        locationButton.setRequestLocation(true);
        row.add(locationButton);
        buttons.add(row);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setKeyboard(buttons);
        return keyboard;
    }

    private ReplyKeyboardMarkup getMovieRandomizerKeyboard() {
        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton randomMovieButton = new KeyboardButton("Рандомный фильм");
        KeyboardButton genresButton = new KeyboardButton("Обнулить жанры");
        KeyboardButton backButton = new KeyboardButton("Back");
        row.add(randomMovieButton);
        row2.add(genresButton);
        row2.add(backButton);
        buttons.add(row);
        buttons.add(row2);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setKeyboard(buttons);
        return keyboard;
    }

    private InlineKeyboardMarkup getMinesweeperKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow2 = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                InlineKeyboardButton button = new InlineKeyboardButton().setText(" " + new String(Character.toChars(0x2B1B)) + " ").setCallbackData("{\"x\":" + i + ",\"y\":" + j + "}");
                row.add(button);
            }
            buttons.add(row);
        }
        buttonsRow1.add(new InlineKeyboardButton().setText("Открыть").setCallbackData("/flag"));
        buttonsRow2.add(new InlineKeyboardButton().setText("Новая игра").setCallbackData(MenuState.MINESWEEPER.getName()));
        buttonsRow2.add(new InlineKeyboardButton().setText("< Back").setCallbackData(MenuState.GAMES_MENU.getName()));
        buttons.add(buttonsRow1);
        buttons.add(buttonsRow2);
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup().setKeyboard(buttons);

        return new InlineKeyboardMarkup().setKeyboard(buttons);
    }

    private ReplyKeyboardMarkup getKudaGoCitiesKeyboard() {
        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardRow row5 = new KeyboardRow();
        KeyboardButton buttonMoscow = new KeyboardButton("Москва");
        KeyboardButton buttonSpb = new KeyboardButton("Санкт-Петербург");
        KeyboardButton buttonEkb = new KeyboardButton("Екатеринбург");
        KeyboardButton buttonKrasn = new KeyboardButton("Красноярск");
        KeyboardButton buttonKrsnd = new KeyboardButton("Краснодар");
        KeyboardButton buttonNzjn = new KeyboardButton("Нижний Новгород");
        KeyboardButton buttonNovos = new KeyboardButton("Новосибирск");
        KeyboardButton buttonSochi = new KeyboardButton("Сочи");
        KeyboardButton backButton = new KeyboardButton("Back");
        row.add(buttonMoscow);
        row.add(buttonSpb);
        row2.add(buttonEkb);
        row2.add(buttonKrasn);
        row3.add(buttonKrsnd);
        row3.add(buttonNzjn);
        row4.add(buttonNovos);
        row4.add(buttonSochi);
        row5.add(backButton);
        buttons.add(row);
        buttons.add(row2);
        buttons.add(row3);
        buttons.add(row4);
        buttons.add(row5);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(false);
        keyboard.setKeyboard(buttons);
        return keyboard;
    }

    private ReplyKeyboardMarkup getKudaGoKeyboard() {
        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton placesButton = new KeyboardButton("Места");
        KeyboardButton eventsButton = new KeyboardButton("События");
        KeyboardButton backButton = new KeyboardButton("Back");
        row.add(placesButton);
        row.add(eventsButton);
        row2.add(backButton);
        buttons.add(row);
        buttons.add(row2);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setKeyboard(buttons);
        return keyboard;
    }

    private ReplyKeyboard getKeyboard(){
        return statesInfo.get(model.getMenuState()).keyboard;
    }

    @Override
    public String getBotUsername() {
        return System.getenv("TELEGRAM_BOT_NAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_BOT_TOKEN");
    }
}
