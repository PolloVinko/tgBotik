package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.bot.validator.SubscriberValidator;
import com.skillbox.cryptobot.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SubscribeCommand implements IBotCommand {
    private final SubscriberService subscriberService;
    private final SubscriberValidator subscriberValidator;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        if(subscriberValidator.isBigDecimal(arguments)){
            answer.setText("Новая подписка создана на стоимость " + arguments[0] + " USD");
            subscriberService.updatePrice(arguments[0], message.getFrom().getUserName());
        }
        else{
            answer.setText("Введите цену в USD (только цифры и точка).\nПример: 10 или 10.50");
        }

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }
}