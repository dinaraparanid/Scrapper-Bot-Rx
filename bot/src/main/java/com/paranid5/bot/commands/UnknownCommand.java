package com.paranid5.bot.commands;

import com.paranid5.bot.messages.UnknownCommandMessage;
import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.SendResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public final class UnknownCommand implements BotCommand<SendResponse> {
    @Override
    public @NonNull Single<SendResponse> onCommand(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return Single.fromCallable(() ->
            bot.execute(
                UnknownCommandMessage
                    .unknownCommandMessage(Messages.chatId(message))
            )
        );
    }
}
