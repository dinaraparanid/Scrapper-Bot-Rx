package com.paranid5.bot;

import com.paranid5.bot.configuration.BotConfig;
import com.paranid5.bot.interactor.TgBotInteractor;
import com.paranid5.bot.messages.AlreadyTrackingMessage;
import com.paranid5.bot.messages.LinkNotTrackedMessage;
import com.paranid5.bot.messages.StartTrackingMessage;
import com.paranid5.bot.messages.StopTrackingMessage;
import com.paranid5.bot.messages.UnsupportedLinkMessage;
import com.paranid5.core.bot.ScrapperBot;
import com.paranid5.core.entities.link.LinkResponse;
import com.paranid5.core.entities.user.User;
import com.paranid5.core.entities.user.UserId;
import com.paranid5.core.entities.user.UserState;
import com.paranid5.data.link.repository.LinkRepository;
import com.paranid5.data.user.UserDataSource;
import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public final class ScrapperBotImpl implements ScrapperBot {
    private record States_Trackings_Message(
        Map<UserId, UserState> states,
        Map<UserId, List<String>> trackings,
        Message message
    ) {}

    private record ResponseWithUsers(
        LinkResponse response,
        Collection<User> users
    ) {}

    private final String botToken;
    private final UserDataSource userDataSource;
    private final LinkRepository linkRepository;
    private final TgBotInteractor botInteractor;

    private final Subject<Message> messagesSource =
        PublishSubject.create();

    private final Subject<LinkResponse> linkResponseSource =
        PublishSubject.create();

    public ScrapperBotImpl(
        @NonNull BotConfig config,
        @Qualifier("userDataSourceInMemory")
        UserDataSource userDataSource,
        @Qualifier("linkRepositoryInMemory")
        LinkRepository linkRepository,
        @NonNull TgBotInteractor botInteractor
    ) {
        this.botToken = config.telegramToken();
        this.userDataSource = userDataSource;
        this.linkRepository = linkRepository;
        this.botInteractor = botInteractor;
    }

    @Override
    public void launchBot() {
        final var bot = new TelegramBot(botToken);

        bot.setUpdatesListener(updates -> {
            updates
                .stream()
                .map(Update::message)
                .forEach(messagesSource::onNext);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        setCommands(bot);
        launchBotEventLoop(bot);
        launchResponseMonitoring(bot);
    }

    @Override
    public @NonNull Completable acquireResponse(@NotNull LinkResponse response) {
        return Completable.fromRunnable(() ->
            linkResponseSource.onNext(response)
        );
    }

    private @NonNull Disposable setCommands(@NonNull TelegramBot bot) {
        return Completable
            .fromRunnable(() ->
                    bot.execute(
                        new SetMyCommands(
                            botInteractor
                                .botCommands()
                                .toArray(new BotCommand[0])
                        )
                    )
            )
            .subscribe();
    }

    private @NonNull Disposable launchBotEventLoop(@NonNull TelegramBot bot) {
        return Observable
            .combineLatest(
                userDataSource.userStatesSource(),
                linkRepository.usersTrackingsSource(),
                messagesSource,
                States_Trackings_Message::new
            )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .distinctUntilChanged((old, nev) ->
                old.message.messageId().equals(nev.message.messageId())
            )
            .subscribe(it -> {
                final var user = Messages.botUser(it.message);

                final var userState = it.states.getOrDefault(
                    new UserId(user.id()),
                    new UserState.NoneState(user)
                );

                final var userLinks = it.trackings.getOrDefault(
                    new UserId(user.id()),
                    new ArrayList<>()
                );

                userDataSource
                    .patchUser(user)
                    .andThen(
                        botInteractor
                            .handleCommandAndPatchUserState(bot, it.message, userLinks, userState)
                    )
                    .blockingAwait();
            });
    }

    private @NonNull Disposable launchResponseMonitoring(@NonNull TelegramBot bot) {
        return Observable
            .combineLatest(
                linkResponseSource,
                userDataSource.usersSource(),
                ResponseWithUsers::new
            )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .distinctUntilChanged((old, nev) ->
                old.response.equals(nev.response)
            )
            .subscribe(it -> {
                final var users = it.users;
                final var response = it.response;
                final var chatId = findChatId(users, response);
                bot.execute(responseMessage(chatId, response));
            });
    }

    private long findChatId(
        @NonNull Collection<User> users,
        @NonNull LinkResponse response
    ) {
        return users
            .stream()
            .filter(u -> u.id() == response.getUserId())
            .findFirst()
            .get()
            .chatId();
    }

    private @NonNull SendMessage responseMessage(
        long chatId,
        @NonNull LinkResponse response
    ) {
        return switch (response) {
            case LinkResponse.Invalid l ->
                UnsupportedLinkMessage.unsupportedLinkMessage(chatId, l.getLink());
            case LinkResponse.TrackResponse.New l ->
                StartTrackingMessage.startTrackingMessage(chatId, l.getLink());
            case LinkResponse.UntrackResponse.New l ->
                LinkNotTrackedMessage.linkNotTrackedMessage(chatId, l.getLink());
            case LinkResponse.TrackResponse.Present l ->
                AlreadyTrackingMessage.alreadyTrackingMessage(chatId, l.getLink());
            case LinkResponse.UntrackResponse.Present l ->
                StopTrackingMessage.stopTrackingMessage(chatId, l.getLink());
            default -> throw new IllegalArgumentException("Unknown response " + response);
        };
    }
}
