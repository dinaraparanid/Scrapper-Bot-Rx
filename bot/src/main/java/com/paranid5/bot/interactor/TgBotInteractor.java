package com.paranid5.bot.interactor;

import com.pengrad.telegrambot.model.BotCommand;
import io.reactivex.rxjava3.annotations.NonNull;
import java.util.List;

public interface TgBotInteractor extends BotInteractor {
    @NonNull
    List<BotCommand> botCommands();
}
