package com.paranid5.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot", ignoreUnknownFields = false)
public record BotConfig(String telegramToken) {}
