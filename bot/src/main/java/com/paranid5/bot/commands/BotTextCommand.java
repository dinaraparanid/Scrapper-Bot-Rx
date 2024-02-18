package com.paranid5.bot.commands;

import io.reactivex.rxjava3.annotations.NonNull;

public interface BotTextCommand<T> extends BotCommand<T> {
    boolean matches(@NonNull String command);
}
