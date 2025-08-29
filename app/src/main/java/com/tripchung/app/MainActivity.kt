package com.tripchung.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tripchung.app.ui.*
import com.tripchung.app.ui.nav.Routes
import com.tripchung.app.ui.nav.TripChungBottomBar
import com.tripchung.app.ui.theme.TripChungTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TripChungTheme { TripChungApp() } }
    }
}

@Composable
fun TripChungApp() {
    val nav = rememberNavController()
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            TripChungBottomBar(
                navController = nav,
                currentDestination = currentDestination
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            NavHost(navController = nav, startDestination = Routes.HOME) {

                composable(Routes.HOME) {
                    HomeScreen(onPlanClick = { nav.navigate(Routes.PLANNER) })
                }

                composable(Routes.PLANNER) {
                    PlannerScreen(
                        onDone = {
                            nav.navigate(Routes.RESULTS) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                composable(Routes.RESULTS) {
                    ResultScreen(
                        onBack = { nav.popBackStack() },
                        onAddToPlan = { /* 일정 저장 처리 */ }
                    )
                }

                // ✅ 주변 탭 → 스샷 동일 화면
                composable(Routes.NEARBY) {
                    NearbyWidgetScreen(onBack = { nav.popBackStack() })
                }

                composable(Routes.SEARCH) { /* SearchScreen() */ }
                composable(Routes.FAVORITES) { /* FavoritesScreen() */ }
                composable(Routes.PROFILE) {
                    MyScreen(
                        onMenuClick = { /* action */ },
                        onSeeAllTrips = { /* 전체보기 */ },
                        onRecentTripClick = { /* 상세 */ }
                    )
                }
            }
        }
    }
}
