package com.paranid5.core.entities.user;

public record User(
    long id,
    long chatId,
    String firstName,
    String lastName
) {}
