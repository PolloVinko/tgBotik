package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.bot.keyboard.KeyboardFactory;
import com.skillbox.cryptobot.bot.validator.SubscriberValidator;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandService {

    private final CryptoCurrencyService service;
    private final StateService stateService;
    private final SubscriberValidator subscriberValidator;
    private final SubscriberService subscriberService;

    public void handleCallback(Update update, CryptoBot bot) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String userName = update.getCallbackQuery().getFrom().getUserName();
        switch (update.getCallbackQuery().getData()) {
            case "GET_PRICE" -> getPrice(chatId, bot);
            case "GET_SUBSCRIPTION" -> getSubscription(userName,chatId,bot);
            case "UNSUBSCRIBE" -> unsubscribe(chatId,userName,bot);
            case "SUBSCRIBE" -> {
                stateService.setState(chatId, "WAITING_FOR_PRICE");
                try {
                    bot.execute(new SendMessage(chatId.toString(), "Введите цену BTC, на которую хотите подписаться:"));
                } catch (Exception e) {
                    log.error("Ошибка при отправке сообщения", e);
                }
            }

        }
    }

    public void handleTextMessage(Message message, CryptoBot bot) {
        String state = stateService.getState(message.getChatId());
        switch (state) {
            case "WAITING_FOR_PRICE" -> subscribe(message, bot);
            default -> {
                if (message.getText().equals("/start")) {
                    start(message, bot);
                } else {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(message.getChatId());
                    msg.setText("🤖 Неизвестная команда. Введите /start");
                    try {
                        bot.execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void start(Message message, CryptoBot bot) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId().toString());
        String userName = message.getFrom().getUserName();
        answer.setText("Привет! Выберите действие:");
        answer.setReplyMarkup(KeyboardFactory.MenuFactory.mainMenu());
        if (!subscriberValidator.isAlreadyExists(userName)) {
            subscriberService.createSubscriber(userName, message.getChatId());
        }
        try {
            bot.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка при отправке стартового меню", e);
        }
    }

    public void getPrice(Long chatId, TelegramLongPollingBot bot) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        try {
            msg.setText("Текущая цена биткоина " + TextUtil.toString(service.getBitcoinPrice()) + " USD");
            bot.execute(msg);
        } catch (Exception e) {
            log.error("Ошибка возникла /get_price методе", e);
        }
    }

    public void subscribe(Message message, CryptoBot bot) {
        Long chatId = message.getChatId();
        String text = message.getText();

        SendMessage msg = new SendMessage();
        msg.setChatId(message.getChatId());
        if (subscriberValidator.isBigDecimal(text)) {
            stateService.clearState(chatId);
            msg.setText("✅ Новая подписка создана на стоимость " + text + " USD");
            subscriberService.updatePrice(message.getText(), message.getFrom().getUserName());
        } else {
            msg.setText("⛔Введите цену в USD (только цифры и точка).\nПример: 10 или 10.50");
        }

        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }
    public void getSubscription(String userName, Long chatId, TelegramLongPollingBot bot){
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId);
        String price = subscriberService.getSubscribePrice(userName);
        answer.setText(price);

        try {
            bot.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }

    public void unsubscribe(Long chatId,String userName, TelegramLongPollingBot bot){
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId);

        answer.setText("Подписка отменена");
        subscriberService.deletePrice(userName);
        try {
           bot.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }
}
