package com.paranid5.bot.messages;

import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

public final class StopTrackingMessage {
    @NotNull
    public static SendMessage stopTrackingMessage(long chatId, @NotNull String link) {
        return new SendMessage(chatId, stopTrackingMessageText(link))
            .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true));
    }

    @NotNull
    private static String stopTrackingMessageText(@NotNull String link) {
        return String.format("Stop tracking %s", link);
    }
}
