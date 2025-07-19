package com.skillbox.cryptobot.bot.validator;

import com.skillbox.cryptobot.bot.repository.SubscribersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriberValidator {
    private final SubscribersRepository subscribersRepository;

    public boolean isAlreadyExists(String id) {
        return subscribersRepository.findByTelegramId(id) != null;
    }

    public boolean isBigDecimal(String argument) {
        return argument.matches("^-?\\d+(\\.\\d+)?$");
    }
}
