package com.paranid5.bot.commands;

import com.paranid5.bot.messages.LinkListMessage;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.SendResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public final class ListCommand implements
    BotTextCommand<SendResponse>,
    ToTgBotCommand {
    private static final String LIST_COMMAND = "/list";
    private static final String LIST_DESCRIPTION = "display a list of tracking links";

    @Override
    public boolean matches(@NonNull String command) {
        return command.equals(LIST_COMMAND);
    }

    @Override
    public @NonNull Single<SendResponse> onCommand(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return Single.fromCallable(() ->
            bot.execute(LinkListMessage.linkListMessage(message, userLinks))
        );
    }

    @Override
    public @NonNull BotCommand toTgBotCommand() {
        return new BotCommand(LIST_COMMAND, LIST_DESCRIPTION);
    }
}
