package com.example.gastosapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gastosapp.presentation.products.ProductScreen
import com.example.gastosapp.presentation.clients.ClientScreen
import com.example.gastosapp.presentation.dashboard.DashboardScreen
import com.example.gastosapp.presentation.expenses.MainExpenseScreen
import com.example.gastosapp.ui.theme.GastosAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            GastosAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBarNavigation(navController = navController)
                    }) { innerPadding ->


                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        startDestination = "expenses"
                    ) {
                        composable("expenses") {
                            MainExpenseScreen()
                        }

                        composable("clients") {
                            ClientScreen()
                        }

                        composable("products") {
                            ProductScreen()
                        }

                        composable("dashboard") {
                            DashboardScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarNavigation(navController: NavHostController){
    val items = listOf(
        BottomNavItem("Expenses", "expenses", icon = R.drawable.expenseicon),
        BottomNavItem("Clients", "clients", icon = R.drawable.clienticon),
        BottomNavItem("Products", "products", icon = R.drawable.box),
        BottomNavItem("Dashboard", "dashboard", icon = R.drawable.dashboardicon)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                modifier = Modifier,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(item.label) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: Int)


