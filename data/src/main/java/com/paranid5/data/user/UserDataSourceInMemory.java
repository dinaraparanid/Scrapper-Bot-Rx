package com.paranid5.data.user;

import com.paranid5.core.entities.user.User;
import com.paranid5.core.entities.user.UserId;
import com.paranid5.core.entities.user.UserState;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public final class UserDataSourceInMemory implements UserDataSource {
    private final Subject<Collection<User>> usersSource =
        PublishSubject.create();

    private final Collection<User> usersState =
        Collections.synchronizedSet(new HashSet<>());

    private final Subject<Map<UserId, UserState>> userStatesSource =
        PublishSubject.create();

    private final Map<UserId, UserState> userStatesState =
        new ConcurrentHashMap<>();

    @Override
    public @NonNull Observable<Collection<User>> usersSource() {
        return usersSource.startWithItem(usersState);
    }

    @Override
    public @NonNull Observable<Map<UserId, UserState>> userStatesSource() {
        return userStatesSource.startWithItem(userStatesState);
    }

    @Override
    public @NonNull Completable patchUser(@NonNull User user) {
        return usersState.contains(user)
            ? Completable.complete()
            : patchUserImpl(user);
    }

    @Override
    public @NonNull Completable patchUserState(@NonNull UserState userState) {
        return patchUserImpl(userState.getUser(), it -> userState);
    }

    private @NonNull Completable patchUserImpl(
        @NonNull User user,
        @NonNull Function<User, UserState> userState
    ) {
        return Completable.fromRunnable(() -> {
            usersState.add(user);
            usersSource.onNext(new ArrayList<>(usersState));

            userStatesState.put(new UserId(user.id()), userState.apply(user));
            userStatesSource.onNext(new HashMap<>(userStatesState));
        });
    }

    private @NonNull Completable patchUserImpl(@NonNull User user) {
        return patchUserImpl(user, UserState.NoneState::new);
    }
}
