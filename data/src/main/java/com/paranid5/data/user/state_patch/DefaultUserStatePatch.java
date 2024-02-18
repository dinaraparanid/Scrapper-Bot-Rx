package com.paranid5.data.user.state_patch;

import com.paranid5.core.entities.user.User;
import com.paranid5.core.entities.user.UserState;
import com.paranid5.data.user.UserDataSource;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import java.util.function.Function;

public final class DefaultUserStatePatch implements UserStatePatch {
    private final UserDataSource source;
    private final Function<User, UserState> userState;

    public DefaultUserStatePatch(
        @NonNull UserDataSource source,
        @NonNull Function<User, UserState> userState
    ) {
        this.source = source;
        this.userState = userState;
    }

    @Override
    public Completable patchUserState(@NonNull User user) {
        return source.patchUserState(userState.apply(user));
    }
}
