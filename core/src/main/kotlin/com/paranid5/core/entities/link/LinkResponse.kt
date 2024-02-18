package com.paranid5.core.entities.link

sealed interface LinkResponse {
    val userId: Long
    val link: String

    sealed interface TrackResponse : LinkResponse {
        data class New @JvmOverloads constructor(
            override val userId: Long,
            override val link: String,
            private val id: Long = System.currentTimeMillis()
        ) : LinkResponse

        data class Present @JvmOverloads constructor(
            override val userId: Long,
            override val link: String,
            private val id: Long = System.currentTimeMillis()
        ) : LinkResponse
    }

    sealed interface UntrackResponse : LinkResponse {
        data class New @JvmOverloads constructor(
            override val userId: Long,
            override val link: String,
            private val id: Long = System.currentTimeMillis()
        ) : LinkResponse

        data class Present @JvmOverloads constructor(
            override val userId: Long,
            override val link: String,
            private val id: Long = System.currentTimeMillis()
        ) : LinkResponse
    }

    data class Invalid @JvmOverloads constructor(
        override val userId: Long,
        override val link: String,
        private val id: Long = System.currentTimeMillis()
    ) : LinkResponse
}
