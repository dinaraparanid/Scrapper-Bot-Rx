package com.paranid5.bot.messages;

import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

public final class UnknownCommandMessage {
    @NotNull
    public static SendMessage unknownCommandMessage(long chatId) {
        return new SendMessage(chatId, unknownCommandMessageText());
    }

    @NotNull
    private static String unknownCommandMessageText() {
        return "Unknown command";
    }
}
