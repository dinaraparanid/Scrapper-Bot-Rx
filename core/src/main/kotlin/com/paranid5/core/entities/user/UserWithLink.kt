package com.paranid5.core.entities.user

import kotlin.random.Random

data class UserWithLink @JvmOverloads constructor(
    val userId: Long,
    val link: String,
    private val id: Int = Random.nextInt()
)
