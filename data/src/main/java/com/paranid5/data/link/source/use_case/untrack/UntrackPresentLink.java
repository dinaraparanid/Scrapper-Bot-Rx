package com.paranid5.data.link.source.use_case.untrack;

import com.paranid5.core.entities.link.types.LinkType;
import com.paranid5.data.link.response.LinkResponseChannel;
import com.paranid5.data.link.response.LinkResponseChannels;
import com.paranid5.data.link.source.LinkDataSource;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import java.util.ArrayList;
import java.util.List;

public final class UntrackPresentLink {
    @NonNull
    public static Completable onUntrackPresent(
        @NonNull LinkDataSource source,
        long userId,
        @NonNull LinkType link,
        @NonNull List<String> links,
        @NonNull LinkResponseChannel channel
    ) {
        final var newLinks = new ArrayList<>(links);
        newLinks.remove(link.getLink());

        return source
            .patchTrackings(userId, newLinks)
            .andThen(
                LinkResponseChannels
                    .respondUntrackLinkPresent(channel, userId, link)
            );
    }
}
