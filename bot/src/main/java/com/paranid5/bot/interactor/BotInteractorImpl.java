package com.paranid5.bot.interactor;

import com.paranid5.bot.commands.BotStatusCommand;
import com.paranid5.bot.commands.BotTextCommand;
import com.paranid5.bot.commands.HelpCommand;
import com.paranid5.bot.commands.ListCommand;
import com.paranid5.bot.commands.StartCommand;
import com.paranid5.bot.commands.ToTgBotCommand;
import com.paranid5.bot.commands.TrackCommand;
import com.paranid5.bot.commands.TrackLinkCommand;
import com.paranid5.bot.commands.UnknownCommand;
import com.paranid5.bot.commands.UntrackCommand;
import com.paranid5.bot.commands.UntrackLinkCommand;
import com.paranid5.core.entities.link.parsers.Parsers;
import com.paranid5.core.entities.user.UserState;
import com.paranid5.data.link.repository.LinkRepository;
import com.paranid5.data.user.state_patch.HelpStatePatch;
import com.paranid5.data.user.state_patch.ListStatePatch;
import com.paranid5.data.user.state_patch.StartStatePatch;
import com.paranid5.data.user.state_patch.TrackLinkStatePatch;
import com.paranid5.data.user.state_patch.TrackStatePatch;
import com.paranid5.data.user.state_patch.UntrackLinkStatePatch;
import com.paranid5.data.user.state_patch.UntrackStatePatch;
import com.paranid5.data.user.state_patch.UserStatePatch;
import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.SendResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public final class BotInteractorImpl implements TgBotInteractor {
    private record StateLessCommandAndPatcher<C extends BotTextCommand<SendResponse> & ToTgBotCommand>(
        C command,
        UserStatePatch patch
    ) {}

    private record StateFullCommandAndPatcher(
        BotStatusCommand<Optional<Object>> command,
        UserStatePatch patch
    ) {}

    private final Parsers parsers = new Parsers();
    private final UnknownCommand unknownCommand = new UnknownCommand();

    private final LinkRepository linkRepository;
    private final HelpStatePatch helpStatePatch;
    private final ListStatePatch listStatePatch;
    private final StartStatePatch startStatePatch;
    private final TrackLinkStatePatch trackLinkStatePatch;
    private final TrackStatePatch trackStatePatch;
    private final UntrackStatePatch untrackStatePatch;
    private final UntrackLinkStatePatch untrackLinkStatePatch;

    private final List<StateLessCommandAndPatcher<?>> stateLessCommands;
    private final Map<String, StateFullCommandAndPatcher> stateFullCommands;

    @NonNull
    private List<StateLessCommandAndPatcher<?>> stateLessCommands() {
        return List.of(
            new StateLessCommandAndPatcher<>(new HelpCommand(), helpStatePatch),
            new StateLessCommandAndPatcher<>(new ListCommand(), listStatePatch),
            new StateLessCommandAndPatcher<>(new StartCommand(), startStatePatch),
            new StateLessCommandAndPatcher<>(new TrackCommand(), trackStatePatch),
            new StateLessCommandAndPatcher<>(new UntrackCommand(), untrackStatePatch)
        );
    }

    @NonNull
    private Map<String, StateFullCommandAndPatcher> stateFullCommands() {
        return Map.of(
            UserState.TrackSentState.class.getSimpleName(),
            new StateFullCommandAndPatcher(new TrackLinkCommand(linkRepository, parsers), trackLinkStatePatch),
            UserState.UntrackSentState.class.getSimpleName(),
            new StateFullCommandAndPatcher(new UntrackLinkCommand(linkRepository, parsers), untrackLinkStatePatch)
        );
    }

    public BotInteractorImpl(
        @Qualifier("linkRepositoryInMemory")
        LinkRepository linkRepository,
        HelpStatePatch helpStatePatch,
        ListStatePatch listStatePatch,
        StartStatePatch startStatePatch,
        TrackLinkStatePatch trackLinkStatePatch,
        TrackStatePatch trackStatePatch,
        UntrackStatePatch untrackStatePatch,
        UntrackLinkStatePatch untrackLinkStatePatch
    ) {
        this.linkRepository = linkRepository;
        this.helpStatePatch = helpStatePatch;
        this.listStatePatch = listStatePatch;
        this.startStatePatch = startStatePatch;
        this.trackLinkStatePatch = trackLinkStatePatch;
        this.trackStatePatch = trackStatePatch;
        this.untrackStatePatch = untrackStatePatch;
        this.untrackLinkStatePatch = untrackLinkStatePatch;

        stateLessCommands = stateLessCommands();
        stateFullCommands = stateFullCommands();
    }

    @Override
    public @NonNull List<BotCommand> botCommands() {
        return stateLessCommands
            .stream()
            .map(it -> it.command.toTgBotCommand())
            .toList();
    }

    @Override
    public @NonNull Completable handleCommandAndPatchUserState(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks,
        @NonNull UserState userState
    ) {
        final var commandPatcher = findStateLessCommand(Messages.textOrEmpty(message));
        return handleCommandAndPatchUserStateImpl(commandPatcher, bot, message, userLinks, userState);
    }

    @NonNull
    private Optional<StateLessCommandAndPatcher<?>> findStateLessCommand(@NonNull String command) {
        return stateLessCommands
            .stream()
            .filter(it -> it.command.matches(command))
            .findFirst();
    }

    @NonNull
    private Completable handleCommandAndPatchUserStateImpl(
        @NonNull Optional<StateLessCommandAndPatcher<?>> commandPatcher,
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks,
        @NonNull UserState userState
    ) {
        return commandPatcher
            .map(it -> handleStateLessCommands(it, bot, message, userLinks))
            .orElseGet(() -> handleStateFullCommands(bot, message, userLinks, userState));
    }

    @NonNull
    private Completable handleStateFullCommands(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks,
        @NonNull UserState userState
    ) {
        final var handler = stateFullCommands.get(userState.getClass().getSimpleName());

        return handler != null
            ? onStateFullCommandFound(handler.command, handler.patch, bot, message, userLinks)
            : Completable.fromSingle(unknownCommand.onCommand(bot, message, userLinks));
    }

    @NonNull
    private Completable onStateFullCommandFound(
        @NonNull BotStatusCommand<Optional<Object>> command,
        @NonNull UserStatePatch patch,
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return Completable.fromSingle(
            command
                .onCommand(bot, message, userLinks)
                .observeOn(Schedulers.io())
                .map(res ->
                    res
                        .map(it ->
                            patch
                                .patchUserState(Messages.botUser(message))
                                .subscribeOn(Schedulers.io())
                                .andThen(Single.just(new Object()))
                                .blockingGet()
                        )
                        .orElseGet(() ->
                            command
                                .onFailure(bot, message)
                                .subscribeOn(Schedulers.io())
                                .blockingGet()
                        )
                )
        );
    }

    @NonNull
    private Completable handleStateLessCommands(
        @NonNull StateLessCommandAndPatcher<?> handler,
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return Completable
            .fromSingle(handler.command.onCommand(bot, message, userLinks))
            .andThen(handler.patch.patchUserState(Messages.botUser(message)));
    }
}
