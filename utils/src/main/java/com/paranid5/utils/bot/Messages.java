package com.paranid5.utils.bot;

import com.paranid5.core.entities.user.User;
import com.pengrad.telegrambot.model.Message;
import org.jetbrains.annotations.NotNull;

public final class Messages {
    @NotNull
    public static User botUser(@NotNull Message message) {
        return new User(
            userId(message),
            chatId(message),
            name(message),
            family(message)
        );
    }

    public static long userId(@NotNull Message message) {
        return message.from().id();
    }

    @NotNull
    public static String name(@NotNull Message message) {
        return message.from().firstName();
    }

    @NotNull
    public static String family(@NotNull Message message) {
        return message.from().lastName();
    }

    public static long chatId(@NotNull Message message) {
        return message.chat().id();
    }

    @NotNull
    public static String textOrEmpty(@NotNull Message message) {
        final var text = message.text();
        return text != null ? text : "";
    }
}
