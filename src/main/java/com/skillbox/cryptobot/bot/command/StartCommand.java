package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.bot.keyboard.KeyboardFactory;
import com.skillbox.cryptobot.bot.validator.SubscriberValidator;
import com.skillbox.cryptobot.service.SubscriberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


/**
 * Обработка команды начала работы с ботом
 */
@Service
@AllArgsConstructor
@Slf4j

public class StartCommand implements IBotCommand {
    private final SubscriberService subscriberService;
    private final SubscriberValidator subscriberValidator;
    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        String userName = message.getFrom().getUserName();
        if (!subscriberValidator.isAlreadyExists(userName)) {
            subscriberService.createSubscriber(userName, message.getChatId());
        }
        answer.setText("""
                Привет! Данный бот помогает отслеживать стоимость биткоина.
                Поддерживаемые команды:
                 /get_price - получить стоимость биткоина
                 /subscribe - [число] - подписаться на стоимость биткоина в USD
                 /get_subscription - получить текущую подписку
                 /unsubscribe - отменить подписку на стоимость
                """);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }
}