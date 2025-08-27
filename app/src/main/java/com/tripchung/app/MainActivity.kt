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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.tripchung.app.ui.HomeScreen
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
        // ⭕️ 플래너 포함 모든 화면에서 공용 바텀바 노출
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
                    // ⭕️ 설문 완료 시 어디로 갈지만 위임
                    PlannerScreen(
                        onDone = { /* answers ->
                            필요하면 answers 사용 */
                            nav.navigate(Routes.HOME) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                composable(Routes.RESULTS) {
                    ResultScreen(
                        onAdd = { /* TODO: 일정에 추가 처리 */ },
                        onToggleWish = { /* TODO: 찜 토글 처리 */ }
                    )
                }

                composable(Routes.SEARCH) { /* SearchScreen() */ }
                composable(Routes.NEARBY) { /* NearbyScreen() */ }
                composable(Routes.FAVORITES) { /* FavoritesScreen() */ }
                composable(Routes.PROFILE) { /* ProfileScreen() */ }
            }
        }
    }
}
