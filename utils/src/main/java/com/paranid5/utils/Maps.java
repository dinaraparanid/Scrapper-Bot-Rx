package com.paranid5.utils;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public final class Maps {
    public static <F, S> @NotNull Map<F, List<S>> concat(
        @NotNull Map<F, List<S>> lhs,
        @NotNull Map<F, List<S>> rhs
    ) {
        final var res = new HashMap<>(lhs);

        rhs.forEach((key, vals) -> {
            final var present = new ArrayList<>(lhs.getOrDefault(key, new ArrayList<>()));
            present.addAll(vals);
            res.put(key, present);
        });

        return res;
    }
}
