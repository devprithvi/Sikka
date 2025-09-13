package com.prithvi.sikka

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform