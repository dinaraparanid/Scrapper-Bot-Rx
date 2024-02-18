package com.paranid5.core.entities.link;

import com.paranid5.core.entities.link.types.LinkType;
import java.util.Optional;

public interface LinkParser {
    Optional<LinkType> parseLink(final String link);
}
