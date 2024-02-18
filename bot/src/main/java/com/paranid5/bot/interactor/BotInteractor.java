package com.paranid5.bot.interactor;

import com.paranid5.core.entities.user.UserState;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import java.util.List;

public interface BotInteractor {
    @NonNull
    Completable handleCommandAndPatchUserState(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks,
        @NonNull UserState userState
    );
}
