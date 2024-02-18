package com.paranid5.data.link.source.use_case.track;

import com.paranid5.core.entities.link.types.LinkType;
import com.paranid5.data.link.response.LinkResponseChannel;
import com.paranid5.data.link.response.LinkResponseChannels;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;

public final class TrackPresentLink {
    @NonNull
    public static Completable onTrackPresentLink(
        long userId,
        @NonNull LinkType link,
        @NonNull LinkResponseChannel channel
    ) {
        return LinkResponseChannels
            .respondTrackLinkPresent(channel, userId, link);
    }
}
