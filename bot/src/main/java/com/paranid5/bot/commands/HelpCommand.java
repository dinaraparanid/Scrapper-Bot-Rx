package com.paranid5.bot.commands;

import com.paranid5.bot.messages.HelpMessage;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.SendResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public class HelpCommand implements BotTextCommand<SendResponse>, ToTgBotCommand {
    private static final String HELP_COMMAND = "/help";
    private static final String HELP_DESCRIPTION = "display a list of commands";

    @Override
    public boolean matches(@NonNull String command) {
        return command.equals(HELP_COMMAND);
    }

    @Override
    public @NonNull Single<SendResponse> onCommand(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return Single.fromCallable(() ->
            bot.execute(HelpMessage.helpMessage(message))
        );
    }

    @Override
    public @NonNull BotCommand toTgBotCommand() {
        return new BotCommand(HELP_COMMAND, HELP_DESCRIPTION);
    }
}
