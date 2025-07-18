package com.skillbox.cryptobot.bot;

import com.skillbox.cryptobot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
public class CryptoBot extends TelegramLongPollingBot {

    private final CommandService commandService;
    private final String botUsername;
    private final String botToken;

    public CryptoBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            CommandService commandService
    ) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.commandService = commandService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            commandService.start(update.getMessage().getChatId(), this);

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            commandService.handleCallback(callbackData, chatId, this);
        }
    }
}
