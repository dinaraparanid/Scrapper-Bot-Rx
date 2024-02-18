package com.paranid5.core.entities.link.parsers;

import com.paranid5.core.entities.link.LinkParser;
import com.paranid5.core.entities.link.types.StackOverflowLinkType;
import com.paranid5.core.entities.link.types.LinkType;
import io.reactivex.rxjava3.annotations.NonNull;
import java.util.Optional;

public final class StackOverflowParser implements LinkParser {
    private static final String STACK_OVERFLOW_REGEX = "https://stackoverflow\\.com/.*";

    @Override
    public Optional<LinkType> parseLink(@NonNull String link) {
        return link.matches(STACK_OVERFLOW_REGEX)
            ? Optional.of(new StackOverflowLinkType(link))
            : Optional.empty();
    }
}
