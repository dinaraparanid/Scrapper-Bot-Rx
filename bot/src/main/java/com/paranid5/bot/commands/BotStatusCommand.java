package com.paranid5.bot.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

public interface BotStatusCommand<T> extends BotCommand<T> {
    @NonNull
    Single<T> onFailure(
        @NonNull TelegramBot bot,
        @NonNull Message message
    );
}
