package com.tripchung.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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

    // 설문/결과 공유용 VM
    val plannerVM: PlannerViewModel = viewModel()

    Scaffold(
        bottomBar = {
            // 로그인/플래너에서는 하단바 숨김
            val hideOn = setOf(Routes.LOGIN, Routes.PLANNER)
            if (currentDestination?.route !in hideOn && currentDestination != null) {
                TripChungBottomBar(
                    navController = nav,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {

            NavHost(
                navController = nav,
                startDestination = Routes.HOME
            ) {
                // ✅ 로그인 화면 실제 연결
                composable(Routes.LOGIN) {
                    LoginScreen(
                        onLoginSuccess = {
                            nav.navigate(Routes.HOME) {
                                // 로그인 화면 제거 + 상태 복원
                                popUpTo(nav.graph.findStartDestination().id) { inclusive = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onBrowse = { // “둘러보기” 버튼 등
                            nav.navigate(Routes.HOME) {
                                popUpTo(nav.graph.findStartDestination().id) { inclusive = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                composable(Routes.HOME) {
                    HomeScreen(onPlanClick = { nav.navigate(Routes.PLANNER) })
                }

                composable(Routes.PLANNER) {
                    PlannerScreen(
                        plannerViewModel = plannerVM,
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
                        plannerViewModel = plannerVM,
                        onBack = { nav.popBackStack() },
                        onAddToPlan = { /* TODO: 일정 저장 */ }
                    )
                }

                composable(Routes.SEARCH) { /* SearchScreen() */ }
                composable(Routes.NEARBY) {
                    NearbyWidgetScreen(onBack = { nav.popBackStack() })
                }
                composable(Routes.FAVORITES) {
                    FavoritesScreen(
                        onAddPlaceToPlan = { /* TODO */ },
                        onOpenPlan = { /* TODO */ },
                        onDuplicatePlan = { /* TODO */ },
                        onSharePlace = { /* TODO */ },
                        onSharePlan = { /* TODO */ },
                        onEditTap = { /* TODO */ }
                    )
                }
                composable(Routes.PROFILE) {
                    MyScreen(
                        onMenuClick = { /* TODO */ },
                        onSeeAllTrips = { /* TODO */ },
                        onRecentTripClick = { /* TODO */ }
                    )
                }
            }
        }
    }
}
