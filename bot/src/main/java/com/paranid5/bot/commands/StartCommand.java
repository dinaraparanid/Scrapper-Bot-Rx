package com.paranid5.bot.commands;

import com.paranid5.bot.messages.RegisteredMessage;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.SendResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public final class StartCommand implements
    BotTextCommand<SendResponse>,
    ToTgBotCommand {
    private static final String START_COMMAND = "/start";
    private static final String START_DESCRIPTION = "register a user";

    @Override
    public boolean matches(@NonNull String command) {
        return command.equals(START_COMMAND);
    }

    @Override
    public @NonNull Single<SendResponse> onCommand(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return Single.fromCallable(() ->
            bot.execute(RegisteredMessage.registeredMessage(message))
        );
    }

    @Override
    public @NonNull BotCommand toTgBotCommand() {
        return new BotCommand(START_COMMAND, START_DESCRIPTION);
    }
}
