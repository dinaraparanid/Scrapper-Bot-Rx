package com.paranid5.data.link.source;

import com.paranid5.core.entities.user.UserId;
import com.paranid5.data.link.response.LinkResponseChannel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.List;
import java.util.Map;

public interface LinkDataSource {
    @NonNull
    Observable<Map<UserId, List<String>>> usersTrackingsSource();

    @NonNull
    Completable patchTrackings(long userId, @NonNull List<String> links);

    @NonNull
    Completable trackLink(long userId, @NonNull String link);

    @NonNull
    Completable untrackLink(long userId, @NonNull String link);

    @NonNull
    Disposable launchLinksStoreMonitoring(@NonNull LinkResponseChannel channel);

    @NonNull
    Disposable launchLinksRemoveMonitoring(@NonNull LinkResponseChannel channel);
}
