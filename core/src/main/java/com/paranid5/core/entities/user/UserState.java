package com.paranid5.core.entities.user;

public sealed interface UserState {
    User getUser();

    record NoneState(User user) implements UserState {
        @Override
        public User getUser() { return user; }
    }

    record StartSentState(User user) implements UserState {
        @Override
        public User getUser() { return user; }
    }

    record HelpSentState(User user) implements UserState {
        @Override
        public User getUser() { return user; }
    }

    record TrackSentState(User user) implements UserState {
        @Override
        public User getUser() { return user; }
    }

    record TrackLinkSentState(User user) implements UserState {
        @Override
        public User getUser() { return user; }
    }

    record UntrackSentState(User user) implements UserState {
        @Override
        public User getUser() { return user; }
    }

    record UntrackLinkSentState(User user) implements UserState {
        @Override
        public User getUser() { return user; }
    }

    record LinkListSentState(User user) implements UserState {
        @Override
        public User getUser() { return user; }
    }
}
