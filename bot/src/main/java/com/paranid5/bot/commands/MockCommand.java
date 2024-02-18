package com.paranid5.bot.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public record MockCommand(String text) implements BotTextCommand<Object> {
    @Override
    public boolean matches(@NonNull String command) {
        return command.equals(text);
    }

    @Override
    public @NonNull Single<Object> onCommand(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return Single.just(new Object());
    }
}
