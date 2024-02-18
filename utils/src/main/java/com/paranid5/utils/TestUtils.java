package com.paranid5.utils;

import com.paranid5.core.entities.link.LinkResponse;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Map;

public final class TestUtils {
    public static <T> boolean matches(
        @NotNull Collection<T> lhs,
        @NotNull Collection<T> rhs
    ) {
        return rhs.containsAll(lhs) && lhs.containsAll(rhs);
    }

    public static <K, V> boolean matches(
        @NotNull Map<K, V> lhs,
        @NotNull Map<K, V> rhs
    ) {
        return matches(lhs.entrySet(), rhs.entrySet());
    }

    public static boolean matches(
        @NotNull LinkResponse lhs,
        @NotNull LinkResponse rhs
    ) {
        return lhs.getClass().equals(rhs.getClass())
            && lhs.getUserId() == rhs.getUserId()
            && lhs.getLink().equals(rhs.getLink());
    }
}
