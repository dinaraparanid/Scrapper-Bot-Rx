package com.paranid5.data.link.source.github

import com.paranid5.core.entities.link.types.GitHubLinkType
import com.paranid5.data.link.source.DefaultLinkDataSourceInMemory
import com.paranid5.data.link.source.LinkDataSource
import io.reactivex.rxjava3.schedulers.Schedulers
import org.springframework.stereotype.Component

@Component
class GitHubDataSourceInMemory :
    LinkDataSource by DefaultLinkDataSourceInMemory(::GitHubLinkType, Schedulers.io()),
    GitHubDataSource
