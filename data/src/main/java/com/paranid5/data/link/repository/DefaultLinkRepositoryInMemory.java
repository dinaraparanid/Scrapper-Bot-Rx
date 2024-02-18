package com.paranid5.data.link.repository;

import com.paranid5.core.entities.link.types.GitHubLinkType;
import com.paranid5.core.entities.link.types.LinkType;
import com.paranid5.core.entities.link.types.StackOverflowLinkType;
import com.paranid5.core.entities.user.UserId;
import com.paranid5.data.link.response.LinkResponseChannel;
import com.paranid5.data.link.source.github.GitHubDataSource;
import com.paranid5.data.link.source.stackoverflow.StackOverflowDataSource;
import com.paranid5.utils.Maps;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import java.util.Map;

public final class DefaultLinkRepositoryInMemory implements LinkRepository {
    private final GitHubDataSource gitHubDataSource;
    private final StackOverflowDataSource stackOverflowDataSource;

    public DefaultLinkRepositoryInMemory(
        @NonNull GitHubDataSource gitHubDataSource,
        @NonNull StackOverflowDataSource stackOverflowDataSource
    ) {
        this.gitHubDataSource = gitHubDataSource;
        this.stackOverflowDataSource = stackOverflowDataSource;
    }

    @Override
    public @NonNull Observable<Map<UserId, List<String>>> usersTrackingsSource() {
        return Observable.combineLatest(
            gitHubDataSource.usersTrackingsSource(),
            stackOverflowDataSource.usersTrackingsSource(),
            Maps::concat
        );
    }

    @Override
    public @NonNull Completable trackLink(long userId, @NonNull LinkType link) {
        return switch (link) {
            case GitHubLinkType gh -> gitHubDataSource.trackLink(userId, gh.getLink());
            case StackOverflowLinkType so -> stackOverflowDataSource.trackLink(userId, so.getLink());
            default -> throw new IllegalArgumentException("Unexpected value: " + link);
        };
    }

    @Override
    public @NonNull Completable untrackLink(long userId, @NonNull LinkType link) {
        return switch (link) {
            case GitHubLinkType gh -> gitHubDataSource.untrackLink(userId, gh.getLink());
            case StackOverflowLinkType so -> stackOverflowDataSource.untrackLink(userId, so.getLink());
            default -> throw new IllegalArgumentException("Unexpected value: " + link);
        };
    }

    @Override
    public void launchSourceLinksMonitoring(@NonNull LinkResponseChannel channel) {
        gitHubDataSource.launchLinksStoreMonitoring(channel);
        gitHubDataSource.launchLinksRemoveMonitoring(channel);

        stackOverflowDataSource.launchLinksStoreMonitoring(channel);
        stackOverflowDataSource.launchLinksRemoveMonitoring(channel);
    }
}
