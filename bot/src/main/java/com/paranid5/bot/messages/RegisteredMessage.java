package com.paranid5.bot.messages;

import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

public final class RegisteredMessage {
    @NotNull
    public static SendMessage registeredMessage(@NotNull Message message) {
        return new SendMessage(Messages.chatId(message), registeredMessageText(message));
    }

    @NotNull
    private static String registeredMessageText(@NotNull Message message) {
        return String.format(
            "Hello, %s %s!",
            Messages.name(message),
            Messages.family(message)
        );
    }
}
