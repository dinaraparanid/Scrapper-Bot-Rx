package com.paranid5.bot.commands;

import com.paranid5.bot.messages.ProvideUntrackLinkMessage;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.SendResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public final class UntrackCommand implements
    BotTextCommand<SendResponse>,
    ToTgBotCommand {
    private static final String UNTRACK_COMMAND = "/untrack";
    private static final String UNTRACK_DESCRIPTION = "stop link's tracking";

    @Override
    public boolean matches(@NonNull String command) {
        return command.equals(UNTRACK_COMMAND);
    }

    @Override
    public @NonNull Single<SendResponse> onCommand(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return Single.fromCallable(() ->
            bot.execute(
                ProvideUntrackLinkMessage
                    .provideUntrackLinkMessage(message, userLinks)
            )
        );
    }

    @Override
    public @NonNull BotCommand toTgBotCommand() {
        return new BotCommand(UNTRACK_COMMAND, UNTRACK_DESCRIPTION);
    }
}
