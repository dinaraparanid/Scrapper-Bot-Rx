package com.paranid5.data.link.response;

import com.paranid5.core.entities.link.LinkResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;

public final class LinkResponseChannelMock implements LinkResponseChannel {
    @Override
    public @NonNull Completable respondLinkStorage(@NonNull LinkResponse response) {
        return Completable.complete();
    }
}
