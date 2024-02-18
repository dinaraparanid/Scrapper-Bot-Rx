package com.paranid5.bot.messages;

import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.request.SendMessage;
import io.reactivex.rxjava3.annotations.NonNull;

public final class AlreadyTrackingMessage {
    @NonNull
    public static SendMessage alreadyTrackingMessage(long chatId, @NonNull String link) {
        return new SendMessage(chatId, alreadyTrackingMessageText(link))
            .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true));
    }

    @NonNull
    private static String alreadyTrackingMessageText(@NonNull String link) {
        return String.format("You are already tracking %s", link);
    }
}
