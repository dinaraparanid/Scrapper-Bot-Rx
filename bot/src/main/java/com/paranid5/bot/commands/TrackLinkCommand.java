package com.paranid5.bot.commands;

import com.paranid5.bot.messages.UnsupportedLinkMessage;
import com.paranid5.core.entities.link.parsers.Parsers;
import com.paranid5.core.entities.link.types.LinkType;
import com.paranid5.data.link.repository.LinkRepository;
import com.paranid5.utils.bot.Messages;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.Optional;

public final class TrackLinkCommand implements BotStatusCommand<Optional<Object>> {
    private final LinkRepository repository;
    private final Parsers parsers;

    public TrackLinkCommand(
        @NonNull LinkRepository repository,
        @NonNull Parsers parsers
    ) {
        this.repository = repository;
        this.parsers = parsers;
    }

    @Override
    public @NonNull Single<Optional<Object>> onCommand(
        @NonNull TelegramBot bot,
        @NonNull Message message,
        @NonNull List<String> userLinks
    ) {
        return parseLink(message)
            .observeOn(Schedulers.io())
            .map(it ->
                it.map(link ->
                    repository
                        .trackLink(Messages.userId(message), link)
                        .andThen(Single.just(new Object()))
                        .blockingGet()
                )
            );
    }

    @Override
    public @NonNull Single<Optional<Object>> onFailure(
        @NonNull TelegramBot bot,
        @NonNull Message message
    ) {
        return Single
            .fromCallable(() ->
                bot.execute(
                    UnsupportedLinkMessage
                        .unsupportedLinkMessage(
                            Messages.chatId(message),
                            Messages.textOrEmpty(message)
                        )
                )
            )
            .subscribeOn(Schedulers.io())
            .map(it -> Optional.of(new Object()));
    }

    @NonNull
    private Single<Optional<LinkType>> parseLink(
        @NonNull Message message
    ) {
        return Single.fromCallable(() ->
            parsers.parseLink(Messages.textOrEmpty(message))
        );
    }
}
