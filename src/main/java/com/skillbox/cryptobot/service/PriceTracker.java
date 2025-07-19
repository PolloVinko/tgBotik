package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.model.Subscriber;
import com.skillbox.cryptobot.bot.repository.SubscribersRepository;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceTracker {
    private final SubscribersRepository subscribersRepository;
    private final SubscriberService subscriberService;
    private final CryptoCurrencyService cryptoCurrencyService;
    private final AbsSender absSender;

    @Scheduled(fixedRate = 120000)
    public void priceTracking() throws IOException {

        BigDecimal price = cryptoCurrencyService.getBitcoinPrice();
        Instant tenMinutesAgo = Instant.now().minus(Duration.ofMinutes(10));
        List<Subscriber> subscribers = subscribersRepository
                .findByPriceGreaterThanEqualAndNotificationTimeLessThan(price, tenMinutesAgo);
        subscribers.stream().forEach(subscriber -> {
            try {
                getMessage(subscriber);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void getMessage(Subscriber subscriber) throws IOException {
        BigDecimal price = cryptoCurrencyService.getBitcoinPrice();
        SendMessage msg = new SendMessage();
        msg.setChatId(subscriber.getChatId());
        msg.setText("Пора покупать, стоимость биткоина  " + TextUtil.toString(price) + " USD");
        subscriberService.updateNotificationTime(subscriber.getUuid());

        try {
            absSender.execute(msg);
        } catch (TelegramApiException e) {
            log.error("Error", e);
        }
    }
}
