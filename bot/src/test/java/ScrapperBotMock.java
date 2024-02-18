import com.paranid5.bot.interactor.BotInteractor;
import com.paranid5.core.bot.ScrapperBot;
import com.paranid5.core.entities.link.LinkResponse;
import com.paranid5.core.entities.user.User;
import com.paranid5.core.entities.user.UserId;
import com.paranid5.core.entities.user.UserState;
import com.paranid5.data.link.repository.LinkRepository;
import com.paranid5.data.user.UserDataSource;
import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import kotlin.random.Random;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class ScrapperBotMock implements ScrapperBot {
    private record States_Trackings_Message(
        Map<UserId, UserState> states,
        Map<UserId, List<String>> trackings,
        Message message
    ) {}

    private record ResponseWithUsers(
        LinkResponse response,
        Collection<User> users
    ) {}

    private final UserDataSource userDataSource;
    private final LinkRepository linkRepository;
    private final BotInteractor botInteractor;

    private final Subject<Message> messagesSource =
        PublishSubject.create();

    private final Subject<LinkResponse> linkResponseSource =
        PublishSubject.create();

    @NonNull
    public Observable<LinkResponse> linkResponseSource() {
        return linkResponseSource;
    }

    public ScrapperBotMock(
        @NonNull UserDataSource userDataSource,
        @NonNull LinkRepository linkRepository,
        @NonNull BotInteractor botInteractor
    ) {
        this.userDataSource = userDataSource;
        this.linkRepository = linkRepository;
        this.botInteractor = botInteractor;
    }

    @Override
    public void launchBot() {
        launchBotEventLoop(new TelegramBot(""));
    }

    @Override
    public @NonNull Completable acquireResponse(@NotNull LinkResponse response) {
        return Completable.fromRunnable(() ->
            linkResponseSource.onNext(response)
        );
    }

    public @NonNull Completable sendStartMessage(@NonNull User user) {
        return sendMessage(user, "/start");
    }

    public @NonNull Completable sendHelpMessage(@NonNull User user) {
        return sendMessage(user, "/help");
    }

    public @NonNull Completable sendTrackMessage(@NonNull User user) {
        return sendMessage(user, "/track");
    }

    public @NonNull Completable sendUntrackMessage(@NonNull User user) {
        return sendMessage(user, "/untrack");
    }

    public @NonNull Completable sendListMessage(@NonNull User user) {
        return sendMessage(user, "/list");
    }

    public @NonNull Completable sendTextMessage(@NonNull User user, @NonNull String text) {
        return sendMessage(user, text);
    }

    private @NonNull Completable sendMessage(
        @NonNull User user,
        @NonNull String message
    ) {
        final var id = Random.Default.nextInt();

        final var msg = new Message() {
            @Override
            public Integer messageId() { return id; }

            @Override
            public String text() { return message; }

            @Override
            public com.pengrad.telegrambot.model.User from() {
                return new com.pengrad.telegrambot.model.User(user.id()) {
                    @Override
                    public String firstName() { return user.firstName(); }

                    @Override
                    public String lastName() { return user.lastName(); }
                };
            }

            @Override
            public Chat chat() {
                return new Chat() {
                    @Override
                    public Long id() { return user.chatId(); }
                };
            }
        };

        return Completable.fromRunnable(() -> messagesSource.onNext(msg));
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
}
