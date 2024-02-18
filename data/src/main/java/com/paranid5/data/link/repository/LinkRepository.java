package com.paranid5.data.link.repository;

import com.paranid5.core.entities.link.types.LinkType;
import com.paranid5.data.link.response.LinkResponseChannel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import java.util.List;
import java.util.Map;
import com.paranid5.core.entities.user.UserId;
import io.reactivex.rxjava3.core.Observable;

public interface LinkRepository {
    @NonNull
    Observable<Map<UserId, List<String>>> usersTrackingsSource();

    @NonNull
    Completable trackLink(long userId, @NonNull LinkType link);

    @NonNull
    Completable untrackLink(long userId, @NonNull LinkType link);

    void launchSourceLinksMonitoring(@NonNull LinkResponseChannel channel);
}
