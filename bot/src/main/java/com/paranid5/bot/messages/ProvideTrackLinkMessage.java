package com.paranid5.bot.messages;

import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

public final class ProvideTrackLinkMessage {
    @NotNull
    public static SendMessage provideTrackLinkMessage(@NotNull Message message) {
        return new SendMessage(Messages.chatId(message), provideTrackLinkMessageText())
            .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true));
    }

    @NotNull
    private static String provideTrackLinkMessageText() {
        return """
        Provide the link to track in URL format:
        https://some_link

        Supported sites:
        1. https://github.com
        2. https://stackoverflow.com""";
    }
}
