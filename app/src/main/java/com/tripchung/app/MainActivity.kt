package com.tripchung.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tripchung.app.ui.HomeScreen
import com.tripchung.app.ui.PlannerScreen
import com.tripchung.app.ui.theme.TripChungTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripChungTheme { TripChungApp() }
        }
    }
}

@Composable
fun TripChungApp() {
    val nav = rememberNavController()
    Scaffold { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            NavHost(navController = nav, startDestination = "home") {
                composable("home") {
                    HomeScreen(onPlanClick = { nav.navigate("planner") })
                }
                composable("planner") {
                    // PlannerScreen은 onNavChange만 받습니다.
                    PlannerScreen(
                        onNavChange = { route ->
                            // 필요하면 여기서 탭 이동/라우팅 처리
                            // 예: if (route == "home") nav.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
