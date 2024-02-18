package com.paranid5.core.entities.link.types

import kotlin.random.Random

data class StackOverflowLinkType @JvmOverloads constructor(
    override val link: String,
    private val id: Int = Random.nextInt()
) : LinkType
