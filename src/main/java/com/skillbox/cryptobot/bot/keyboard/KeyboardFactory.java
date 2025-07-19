package com.skillbox.cryptobot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyboardFactory {

    public static InlineKeyboardMarkup mainMenu() {
        return createMenu(
                button("📊 Текущая цена", "GET_PRICE"),
                button("🧠 на какую цену я подписан?", "GET_SUBSCRIPTION"),
                button("🔔 Подписаться на цену", "SUBSCRIBE"),
                button("🚫 Отменить подписку", "UNSUBSCRIBE")
        );
    }

    private static InlineKeyboardButton button(String text, String callbackData) {
        InlineKeyboardButton btn = new InlineKeyboardButton(text);
        btn.setCallbackData(callbackData);
        return btn;
    }

    private static InlineKeyboardMarkup createMenu(InlineKeyboardButton... buttons) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (InlineKeyboardButton button : buttons) {
            rows.add(Collections.singletonList(button));
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }
}

