package com.paranid5.core.entities.user;

import io.reactivex.rxjava3.annotations.NonNull;
import java.util.List;

public record UserIdWithLinks(long userId, @NonNull List<String> links) {}
