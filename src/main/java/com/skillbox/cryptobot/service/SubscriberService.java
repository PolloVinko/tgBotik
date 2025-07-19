package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.model.Subscriber;
import com.skillbox.cryptobot.bot.repository.SubscribersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SubscriberService {
    private final SubscribersRepository subscribersRepository;

    public void createSubscriber(String id,  Long chatId) {

        Subscriber subscriber = new Subscriber();
        subscriber.setTelegramId(id);
        subscriber.setChatId(chatId);
        subscriber.setNotificationTime(Instant.now().minus(Duration.ofMinutes(10)));
        subscribersRepository.save(subscriber);
        log.info("New subscriber " + id + " has been created");


    }

    public List<String> getAll(){
        return subscribersRepository.findAll().stream().map(s->s.toString()).toList();
    }

    public void updatePrice(String price, String id){
        Subscriber subscriber = subscribersRepository.findByTelegramId(id);
        BigDecimal bigDecimalPrice = new BigDecimal(price);
        subscriber.setPrice(bigDecimalPrice);
        subscribersRepository.save(subscriber);
    }
    public void updateNotificationTime(Long id){
        Subscriber subscriber = subscribersRepository.findById(id)
                .orElseThrow();
               subscriber.setNotificationTime(Instant.now());
        subscribersRepository.save(subscriber);
    }

    public String getSubscribePrice(String id){
        if(!(subscribersRepository.findByTelegramId(id).getPrice()==null)) {
            String price = subscribersRepository.findByTelegramId(id).getPrice().toString();
            return  "Вы подписаны на стоимость биткоина " + price + " USD";
        }
        return "Активные подписки отсутствуют";
    }

    public void deletePrice(String id){
        Subscriber subscriber = subscribersRepository.findByTelegramId(id);
        subscriber.setPrice(null);
        subscribersRepository.save(subscriber);
    }
}
