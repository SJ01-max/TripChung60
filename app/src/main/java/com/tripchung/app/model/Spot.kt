package com.tripchung.app.model

data class Spot(
    val name: String,
    val minutes: Int,
    val imageUrl: String
)

val sampleSpots = listOf(
    Spot("아산 외암마을", 35, "https://picsum.photos/seed/asan/800/500"),
    Spot("서산 해미읍성", 50, "https://picsum.photos/seed/seosan/800/500"),
    Spot("공주 공산성", 60, "https://picsum.photos/seed/gongju/800/500")
)
