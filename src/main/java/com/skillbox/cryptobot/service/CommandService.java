package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.bot.keyboard.KeyboardFactory;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandService {

    private final CryptoCurrencyService service;

    public void handleCallback(String callback, Long chatId, CryptoBot bot) {
        switch (callback) {
            case "GET_PRICE" -> getPrice(chatId,bot);
//            case "SUBSCRIBE" -> subscribeCommandService.askPrice(chatId, bot);
        }
    }

    public void start(Long chatId, TelegramLongPollingBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Привет! Выберите действие:");
        message.setReplyMarkup(KeyboardFactory.MenuFactory.mainMenu());
        try {
            bot.execute(message);
        } catch (Exception e) {
            log.error("Ошибка при отправке стартового меню", e);
        }
    }

    public void getPrice(Long chatId,TelegramLongPollingBot bot){
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        try {
            msg.setText("Текущая цена биткоина " + TextUtil.toString(service.getBitcoinPrice()) + " USD");
            bot.execute(msg);
        } catch (Exception e) {
            log.error("Ошибка возникла /get_price методе", e);
        }
    }
}
