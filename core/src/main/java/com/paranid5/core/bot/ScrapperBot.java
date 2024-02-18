package com.paranid5.core.bot;

import com.paranid5.core.entities.link.LinkResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;

public interface ScrapperBot {
    void launchBot();

    @NonNull
    Completable acquireResponse(@NonNull LinkResponse response);
}
