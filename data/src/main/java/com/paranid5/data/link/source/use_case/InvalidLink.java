package com.paranid5.data.link.source.use_case;

import com.paranid5.core.entities.link.types.LinkType;
import com.paranid5.data.link.response.LinkResponseChannel;
import com.paranid5.data.link.response.LinkResponseChannels;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;

public final class InvalidLink {
    @NonNull
    public Completable onInvalidLink(
        long userId,
        @NonNull LinkType link,
        @NonNull LinkResponseChannel channel
    ) {
        return LinkResponseChannels
            .respondLinkInvalid(channel, userId, link);
    }
}
