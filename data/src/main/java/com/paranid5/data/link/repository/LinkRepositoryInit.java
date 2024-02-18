package com.paranid5.data.link.repository;

import com.paranid5.data.link.response.LinkResponseChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class LinkRepositoryInit {
    private final LinkRepository repository;
    private final LinkResponseChannel channel;

    public LinkRepositoryInit(
        @Qualifier("linkRepositoryInMemory")
        LinkRepository repository,
        @Qualifier("linkResponseChannelImpl")
        LinkResponseChannel channel
    ) {
        this.repository = repository;
        this.channel = channel;
    }

    @EventListener(ContextRefreshedEvent.class)
    void init() { repository.launchSourceLinksMonitoring(channel); }
}
