package com.paranid5.bot;

import com.paranid5.core.bot.ScrapperBot;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class BotInit {
    private final ScrapperBot bot;

    public BotInit(ScrapperBot bot) {
        this.bot = bot;
    }

    @EventListener(ContextRefreshedEvent.class)
    void init() { bot.launchBot(); }
}
