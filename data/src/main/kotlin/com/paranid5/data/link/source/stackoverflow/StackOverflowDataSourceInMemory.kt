package com.paranid5.data.link.source.stackoverflow

import com.paranid5.core.entities.link.types.StackOverflowLinkType
import com.paranid5.data.link.source.DefaultLinkDataSourceInMemory
import com.paranid5.data.link.source.LinkDataSource
import io.reactivex.rxjava3.schedulers.Schedulers
import org.springframework.stereotype.Component

@Component
class StackOverflowDataSourceInMemory :
    LinkDataSource by DefaultLinkDataSourceInMemory(::StackOverflowLinkType, Schedulers.io()),
    StackOverflowDataSource
