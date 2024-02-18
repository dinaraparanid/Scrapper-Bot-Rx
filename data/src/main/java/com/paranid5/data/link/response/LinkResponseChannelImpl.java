package com.paranid5.data.link.response;

import com.paranid5.core.bot.ScrapperBot;
import com.paranid5.core.entities.link.LinkResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import org.springframework.stereotype.Component;

@Component
public final class LinkResponseChannelImpl implements LinkResponseChannel {
    private final ScrapperBot bot;

    public LinkResponseChannelImpl(@NonNull ScrapperBot bot) {
        this.bot = bot;
    }

    @Override
    @NonNull
    public Completable respondLinkStorage(@NonNull LinkResponse response) {
        return bot.acquireResponse(response);
    }
}
