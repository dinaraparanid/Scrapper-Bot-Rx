package com.paranid5.bot.commands;

import com.paranid5.bot.messages.ProvideTrackLinkMessage;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.SendResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public final class TrackCommand implements
    BotTextCommand<SendResponse>,
    ToTgBotCommand {
    private static final String TRACK_COMMAND = "/track";
    private static final String TRACK_DESCRIPTION = "start link's tracking";

    @Override
    public boolean matches(@NonNull String command) {
        return command.equals(TRACK_COMMAND);
    }

    @Override
    public @NonNull Single<SendResponse> onCommand(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return Single.fromCallable(() ->
            bot.execute(
                ProvideTrackLinkMessage
                    .provideTrackLinkMessage(message)
            )
        );
    }

    @Override
    public @NonNull BotCommand toTgBotCommand() {
        return new BotCommand(TRACK_COMMAND, TRACK_DESCRIPTION);
    }
}
