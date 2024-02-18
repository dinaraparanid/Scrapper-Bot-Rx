package com.paranid5.core.entities.link.parsers;

import com.paranid5.core.entities.link.LinkParser;
import com.paranid5.core.entities.link.types.LinkType;
import io.reactivex.rxjava3.annotations.NonNull;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class Parsers {
    private final List<LinkParser> list = List.of(
        new GitHubParser(),
        new StackOverflowParser()
    );

    @NonNull
    public Optional<LinkType> parseLink(@NonNull String link) {
        return list
            .stream()
            .map(it -> it.parseLink(link))
            .filter(Optional::isPresent)
            .findFirst()
            .flatMap(Function.identity());
    }
}
