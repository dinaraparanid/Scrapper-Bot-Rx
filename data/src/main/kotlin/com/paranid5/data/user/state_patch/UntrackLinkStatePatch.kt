package com.paranid5.data.user.state_patch

import com.paranid5.core.entities.user.UserState
import com.paranid5.data.user.UserDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class UntrackLinkStatePatch(
    @Qualifier("userDataSourceInMemory")
    private val userDataSource: UserDataSource
) : UserStatePatch by DefaultUserStatePatch(userDataSource, UserState::UntrackLinkSentState)

@Component
class UntrackLinkStatePatchMock(
    @Qualifier("userDataSourceInMemory")
    private val userDataSource: UserDataSource
) : UserStatePatch by DefaultUserStatePatch(userDataSource, UserState::UntrackLinkSentState)
