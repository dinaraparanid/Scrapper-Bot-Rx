package com.paranid5.data.link.repository

import com.paranid5.data.link.source.github.GitHubDataSource
import com.paranid5.data.link.source.stackoverflow.StackOverflowDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class LinkRepositoryInMemory(
    @Qualifier("gitHubDataSourceInMemory")
    private val githubDataSource: GitHubDataSource,
    @Qualifier("stackOverflowDataSourceInMemory")
    private val stackOverflowDataSource: StackOverflowDataSource,
) : LinkRepository by DefaultLinkRepositoryInMemory(githubDataSource, stackOverflowDataSource)
