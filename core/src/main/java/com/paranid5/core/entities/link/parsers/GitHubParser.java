package com.paranid5.core.entities.link.parsers;

import com.paranid5.core.entities.link.LinkParser;
import com.paranid5.core.entities.link.types.GitHubLinkType;
import com.paranid5.core.entities.link.types.LinkType;
import io.reactivex.rxjava3.annotations.NonNull;
import java.util.Optional;

public final class GitHubParser implements LinkParser {
    private static final String GITHUB_REGEX = "https://github\\.com/.*";

    @Override
    public Optional<LinkType> parseLink(@NonNull String link) {
        return link.matches(GITHUB_REGEX)
            ? Optional.of(new GitHubLinkType(link))
            : Optional.empty();
    }
}
