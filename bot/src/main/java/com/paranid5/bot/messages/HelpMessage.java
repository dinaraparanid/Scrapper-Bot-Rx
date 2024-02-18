package com.paranid5.bot.messages;

import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import io.reactivex.rxjava3.annotations.NonNull;

public final class HelpMessage {
    @NonNull
    public static SendMessage helpMessage(@NonNull Message message) {
        return new SendMessage(Messages.chatId(message), helpMessageText());
    }

    @NonNull
    private static String helpMessageText() {
        return """
        /start - register a user
        /help - display a list of commands
        /track - start link's tracking
        /untrack - stop link's tracking
        /list - display a list of tracking links""";
    }
}
