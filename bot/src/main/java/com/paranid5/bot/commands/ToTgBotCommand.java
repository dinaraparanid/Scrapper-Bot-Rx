package com.paranid5.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import io.reactivex.rxjava3.annotations.NonNull;

public interface ToTgBotCommand {
    @NonNull
    BotCommand toTgBotCommand();
}
