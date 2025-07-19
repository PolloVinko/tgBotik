package com.skillbox.cryptobot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public class MenuFactory {

        public static InlineKeyboardMarkup mainMenu() {
            InlineKeyboardButton priceButton = new InlineKeyboardButton("📊 Текущая цена");
            priceButton.setCallbackData("GET_PRICE");

            InlineKeyboardButton subscribeButton = new InlineKeyboardButton("🔔 Подписаться на цену");
            subscribeButton.setCallbackData("SUBSCRIBE");

            InlineKeyboardButton getSubscriptionButton = new InlineKeyboardButton("\uD83E\uDDD0 на какую цену я подписан?");
            getSubscriptionButton.setCallbackData("GET_SUBSCRIPTION");

            InlineKeyboardButton unsubscribeButton = new InlineKeyboardButton("\uD83D\uDEAB отменить подписку");
            unsubscribeButton.setCallbackData("UNSUBSCRIBE");

            List<InlineKeyboardButton> row1 = new ArrayList<>();
            row1.add(priceButton);

            List<InlineKeyboardButton> row2 = new ArrayList<>();
            row2.add(getSubscriptionButton);

            List<InlineKeyboardButton> row3 = new ArrayList<>();
            row3.add(subscribeButton);

            List<InlineKeyboardButton> row4 = new ArrayList<>();
            row4.add(unsubscribeButton);

            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            rows.add(row1);
            rows.add(row2);
            rows.add(row3);
            rows.add(row4);

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(rows);
            return markup;
        }
    }
}

