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
import com.tripchung.app.ui.HomeScreen
import com.tripchung.app.ui.MyScreen
import com.tripchung.app.ui.PlannerScreen
import com.tripchung.app.ui.ResultScreen
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
                            // 설문 완료 후 이동 (원하면 결과 화면으로 바꿔도 됨)
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
                        onAddToPlan = { /* placeName -> 일정 저장 처리 */ }
                    )
                }

                // ✅ 프로필 화면: 중복 선언 금지!
                composable(Routes.PROFILE) {
                    MyScreen(
                        onMenuClick = { /* action -> 처리 */ },
                        onSeeAllTrips = { /* 전체보기 이동 */ },
                        // 권장: id 같은 원시값만 넘기도록 MyScreen 시그니처 맞추세요
                        onRecentTripClick = { /* travelId -> 상세 이동 */ }
                    )
                }

                // 다른 탭
                composable(Routes.SEARCH) { /* SearchScreen() */ }
                composable(Routes.NEARBY) { /* NearbyScreen() */ }
                composable(Routes.FAVORITES) { /* FavoritesScreen() */ }
            }
        }
    }
}
