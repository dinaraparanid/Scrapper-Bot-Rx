package com.paranid5.data.user;

import com.paranid5.core.entities.user.User;
import com.paranid5.core.entities.user.UserId;
import com.paranid5.core.entities.user.UserState;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import java.util.Collection;
import java.util.Map;

public interface UserDataSource {
    @NonNull
    Observable<Collection<User>> usersSource();

    @NonNull
    Completable patchUser(@NonNull User user);

    @NonNull
    Observable<Map<UserId, UserState>> userStatesSource();

    @NonNull
    Completable patchUserState(@NonNull UserState userState);
}
