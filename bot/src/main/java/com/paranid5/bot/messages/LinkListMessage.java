package com.paranid5.bot.messages;

import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class LinkListMessage {
    @NotNull
    public static SendMessage linkListMessage(
        @NotNull Message message,
        @NotNull List<String> links
    ) {
        final var msg = links.isEmpty()
            ? emptyLinkListMessageText()
            : nonEmptyLinkListMessageText(links);

        return new SendMessage(Messages.chatId(message), msg)
            .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true));
    }

    @NotNull
    public static String emptyLinkListMessageText() {
        return "You are not tracking anything";
    }

    @NotNull
    private static String nonEmptyLinkListMessageText(@NotNull List<String> links) {
        final var builder = new StringBuilder();

        for (int i = 0; i < links.size(); ++i)
            builder.append(String.format("%d. %s\n", i + 1, links.get(i)));

        return builder.toString();
    }
}
