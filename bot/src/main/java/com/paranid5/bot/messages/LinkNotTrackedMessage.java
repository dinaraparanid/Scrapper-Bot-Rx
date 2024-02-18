package com.paranid5.bot.messages;

import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

public final class LinkNotTrackedMessage {
    @NotNull
    public static SendMessage linkNotTrackedMessage(long charId, @NotNull String link) {
        return new SendMessage(charId, linkNotTrackedMessageText(link))
            .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true));
    }

    @NotNull
    private static String linkNotTrackedMessageText(@NotNull String link) {
        return String.format("You are not tracking %s", link);
    }
}
