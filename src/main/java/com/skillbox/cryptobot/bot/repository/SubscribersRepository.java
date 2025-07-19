package com.skillbox.cryptobot.bot.repository;

import com.skillbox.cryptobot.bot.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SubscribersRepository extends JpaRepository<Subscriber,Long> {
    Subscriber findByTelegramId(String id);
    Boolean existsByTelegramId (String id);
    List<Subscriber> findByPriceGreaterThanEqualAndNotificationTimeLessThan(BigDecimal currentPrice, Instant time);
}
