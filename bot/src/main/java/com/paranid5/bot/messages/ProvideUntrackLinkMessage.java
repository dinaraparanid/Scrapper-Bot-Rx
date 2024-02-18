package com.paranid5.bot.messages;

import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class ProvideUntrackLinkMessage {
    @NotNull
    public static SendMessage provideUntrackLinkMessage(
        @NotNull Message message,
        @NotNull List<String> links
    ) {
        return links.isEmpty()
            ? new SendMessage(Messages.chatId(message), LinkListMessage.emptyLinkListMessageText())
            : new SendMessage(Messages.chatId(message), provideUntrackLinkMessageText())
            .replyMarkup(linksKeyboard(links));
    }

    @NotNull
    private static String provideUntrackLinkMessageText() {
        return "Select the link to untrack";
    }

    @NotNull
    private static ReplyKeyboardMarkup linksKeyboard(@NotNull List<String> links) {
        return new ReplyKeyboardMarkup(
            links
                .stream()
                .map(it -> new String[] { it })
                .toList()
                .toArray(new String[0][])
        )
            .resizeKeyboard(true)
            .oneTimeKeyboard(true);
    }
}
