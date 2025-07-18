package com.skillbox.cryptobot.bot.validator;

import com.skillbox.cryptobot.bot.repository.SubscribersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriberValidator {
    private final SubscribersRepository subscribersRepository;

    public boolean isAlreadyExists(String id){

        if(subscribersRepository.existsByTelegramId(id)){
            return true;
        };
        return false;
    };
    public boolean isBigDecimal(String[] arguments) {
        if (arguments.length == 0) {
            return false; // аргументы не переданы
        }
        return arguments[0].matches("^-?\\d+(\\.\\d+)?$");
    }

}
