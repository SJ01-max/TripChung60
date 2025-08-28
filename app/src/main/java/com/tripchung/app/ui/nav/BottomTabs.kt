package com.tripchung.app.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val NEARBY = "nearby"
    const val FAVORITES = "fav"
    const val PROFILE = "me"
    const val RESULTS = "results"
    const val PLANNER = "planner"   // 바텀탭은 아니지만 화면 라우트

    const val LOGIN = "login"
}

data class BottomTab(val route: String, val label: String, val icon: ImageVector)

val bottomTabs = listOf(
    BottomTab(Routes.HOME, "홈", Icons.Outlined.Home),
    BottomTab(Routes.SEARCH, "검색", Icons.Outlined.Search),
    BottomTab(Routes.NEARBY, "주변", Icons.Outlined.LocationOn),
    BottomTab(Routes.FAVORITES, "찜", Icons.Outlined.FavoriteBorder),
    BottomTab(Routes.PROFILE, "마이", Icons.Outlined.Person),
)
