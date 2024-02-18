package com.paranid5.bot.messages;

import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

public final class UnsupportedLinkMessage {
    @NotNull
    public static SendMessage unsupportedLinkMessage(long chatId, @NotNull String link) {
        return new SendMessage(chatId, unsupportedLinkMessageText(link))
            .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true));
    }

    @NotNull
    private static String unsupportedLinkMessageText(@NotNull String link) {
        return String.format("Link %s is not supported", link);
    }
}
