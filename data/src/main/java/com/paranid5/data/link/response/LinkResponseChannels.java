package com.paranid5.data.link.response;

import com.paranid5.core.entities.link.LinkResponse;
import com.paranid5.core.entities.link.types.LinkType;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import java.util.function.BiFunction;

public final class LinkResponseChannels {
    @NonNull
    public static Completable respondTrackLinkPresent(
        @NonNull LinkResponseChannel channel,
        long userId,
        @NonNull LinkType linkType
    ) {
        return respondLinkImpl(
            channel,
            userId,
            linkType,
            LinkResponse.TrackResponse.Present::new
        );
    }

    @NonNull
    public static Completable respondTrackLinkNew(
        @NonNull LinkResponseChannel channel,
        long userId,
        @NonNull LinkType linkType
    ) {
        return respondLinkImpl(
            channel,
            userId,
            linkType,
            LinkResponse.TrackResponse.New::new
        );
    }

    @NonNull
    public static Completable respondUntrackLinkPresent(
        @NonNull LinkResponseChannel channel,
        long userId,
        @NonNull LinkType linkType
    ) {
        return respondLinkImpl(
            channel,
            userId,
            linkType,
            LinkResponse.UntrackResponse.Present::new
        );
    }

    @NonNull
    public static Completable respondUntrackLinkNew(
        @NonNull LinkResponseChannel channel,
        long userId,
        @NonNull LinkType linkType
    ) {
        return respondLinkImpl(
            channel,
            userId,
            linkType,
            LinkResponse.UntrackResponse.New::new
        );
    }

    @NonNull
    public static Completable respondLinkInvalid(
        @NonNull LinkResponseChannel channel,
        long userId,
        @NonNull LinkType linkType
    ) {
        return respondLinkImpl(
            channel,
            userId,
            linkType,
            LinkResponse.Invalid::new
        );
    }

    @NonNull
    private static Completable respondLinkImpl(
        @NonNull LinkResponseChannel channel,
        long userId,
        @NonNull LinkType link,
        @NonNull BiFunction<Long, String, LinkResponse> response
    ) {
        return channel.respondLinkStorage(response.apply(userId, link.getLink()));
    }
}
