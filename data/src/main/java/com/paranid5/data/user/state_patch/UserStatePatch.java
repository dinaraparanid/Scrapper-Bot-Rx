package com.paranid5.data.user.state_patch;

import com.paranid5.core.entities.user.User;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;

public interface UserStatePatch {
    Completable patchUserState(@NonNull User user);
}
