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

//            InlineKeyboardButton subscribeButton = new InlineKeyboardButton("🔔 Подписаться");
//            subscribeButton.setCallbackData("SUBSCRIBE");

            List<InlineKeyboardButton> row1 = new ArrayList<>();
            row1.add(priceButton);

//            List<InlineKeyboardButton> row2 = new ArrayList<>();
//            row2.add(subscribeButton);

            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            rows.add(row1);
//            rows.add(row2);

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(rows);
            return markup;
        }
    }
}

