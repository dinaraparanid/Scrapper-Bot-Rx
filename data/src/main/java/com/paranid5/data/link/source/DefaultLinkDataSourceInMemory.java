package com.paranid5.data.link.source;

import com.paranid5.core.entities.link.types.LinkType;
import com.paranid5.core.entities.user.UserId;
import com.paranid5.core.entities.user.UserWithLink;
import com.paranid5.data.link.response.LinkResponseChannel;
import com.paranid5.data.link.source.use_case.track.TrackNewLink;
import com.paranid5.data.link.source.use_case.track.TrackPresentLink;
import com.paranid5.data.link.source.use_case.untrack.UntrackNewLink;
import com.paranid5.data.link.source.use_case.untrack.UntrackPresentLink;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class DefaultLinkDataSourceInMemory implements LinkDataSource {
    private record LinkWithTrackings(
        UserWithLink link,
        List<String> trackings
    ) {}

    private record UserId_CurrentLink_Links(
        long userId,
        String currentLink,
        List<String> links
    ) {}

    private final Function<String, LinkType> linkType;

    private final Subject<UserWithLink> storeLinksSource =
        PublishSubject.create();

    private final Subject<UserWithLink> removeLinksSource =
        PublishSubject.create();

    private final Subject<Map<UserId, List<String>>> userTrackingsSource =
        PublishSubject.create();

    private final Map<UserId, List<String>> usersTrackingState =
        new ConcurrentHashMap<>();

    private final Scheduler scheduler;

    public DefaultLinkDataSourceInMemory(
        @NonNull final Function<String, LinkType> linkType,
        @NonNull Scheduler scheduler
    ) {
        this.linkType = linkType;
        this.scheduler = scheduler;
    }

    @Override
    public @NonNull Observable<Map<UserId, List<String>>> usersTrackingsSource() {
        return userTrackingsSource.startWithItem(usersTrackingState);
    }

    @Override
    public @NonNull Completable patchTrackings(long userId, @NonNull List<String> links) {
        return Completable.fromRunnable(() -> {
            usersTrackingState.put(new UserId(userId), links);
            userTrackingsSource.onNext(new HashMap<>(usersTrackingState));
        });
    }

    @Override
    public @NonNull Completable trackLink(long userId, @NonNull String link) {
        return Completable.fromRunnable(() ->
            storeLinksSource.onNext(new UserWithLink(userId, link))
        );
    }

    @Override
    public @NonNull Completable untrackLink(long userId, @NonNull String link) {
        return Completable.fromRunnable(() ->
            removeLinksSource.onNext(new UserWithLink(userId, link))
        );
    }

    @Override
    public @NonNull Disposable launchLinksStoreMonitoring(
        @NonNull LinkResponseChannel channel
    ) {
        return Observable
            .combineLatest(
                storeLinksSource,
                usersTrackingsSource(),
                DefaultLinkDataSourceInMemory::combineLinkWithTrackings
            )
            .subscribeOn(scheduler)
            .observeOn(scheduler)
            .distinctUntilChanged((old, nev) ->
                old.link.equals(nev.link)
            )
            .map(it ->
                new UserId_CurrentLink_Links(
                    it.link.getUserId(),
                    it.link.getLink(),
                    it.trackings
                )
            )
            .subscribe(it -> {
                final var userId = it.userId;
                final var link = it.currentLink;
                final var links = it.links;

                final var task = links.contains(link)
                    ? TrackPresentLink.onTrackPresentLink(userId, linkType.apply(link), channel)
                    : TrackNewLink.onTrackNewLink(this, userId, linkType.apply(link), links, channel);

                task.blockingAwait();
            });
    }

    @Override
    public @NonNull Disposable launchLinksRemoveMonitoring(
        @NonNull LinkResponseChannel channel
    ) {
        return Observable
            .combineLatest(
                removeLinksSource,
                usersTrackingsSource(),
                DefaultLinkDataSourceInMemory::combineLinkWithTrackings
            )
            .subscribeOn(scheduler)
            .observeOn(scheduler)
            .distinctUntilChanged((old, nev) ->
                old.link.equals(nev.link)
            )
            .map(it ->
                new UserId_CurrentLink_Links(
                    it.link.getUserId(),
                    it.link.getLink(),
                    it.trackings
                )
            )
            .subscribe(it -> {
                final var userId = it.userId;
                final var link = it.currentLink;
                final var links = it.links;

                final var task = links.contains(link)
                    ? UntrackPresentLink.onUntrackPresent(this, userId, linkType.apply(link), links, channel)
                    : UntrackNewLink.onUntrackNewLink(userId, linkType.apply(link), channel);

                task.blockingAwait();
            });
    }

    private static @NonNull LinkWithTrackings combineLinkWithTrackings(
        UserWithLink userWithLink,
        @NonNull Map<UserId, List<String>> trackings
    ) {
        return new LinkWithTrackings(
            userWithLink,
            trackings.getOrDefault(
                new UserId(userWithLink.getUserId()),
                new ArrayList<>()
            )
        );
    }
}
