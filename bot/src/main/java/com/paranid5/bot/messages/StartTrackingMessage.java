package com.paranid5.bot.messages;

import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

public final class StartTrackingMessage {
    @NotNull
    public static SendMessage startTrackingMessage(long chatId, @NotNull String link) {
        return new SendMessage(chatId, startTrackingMessageText(link))
            .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true));
    }

    @NotNull
    private static String startTrackingMessageText(@NotNull String link) {
        return String.format("Start tracking %s", link);
    }
}
