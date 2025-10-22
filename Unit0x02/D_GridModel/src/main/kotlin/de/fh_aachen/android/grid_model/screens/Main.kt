// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.grid_model.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.fh_aachen.android.grid_model.ui.theme.FirstAppTheme

@Composable
fun MainScreen() {
     val navController = rememberNavController()

    FirstAppTheme {
        Scaffold(
            //topBar = { TopAppBarWithButtons(drawerState) },
            bottomBar = { BottomAppBarWithButtons(navController = navController) },
        ) { innerPadding ->
            Navigation(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun BottomAppBarWithButtons(navController: NavController) {
    BottomAppBar(
        actions = {
            IconButton(onClick = { navController.navigate(NavDestination.LOGIN.route) }) {
                NavDestination.LOGIN.Icon
            }
            IconButton(onClick = { navController.navigate(NavDestination.BOARD.route) }) {
                NavDestination.BOARD.Icon
            }
        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                val currentRoute = navController.currentBackStackEntry?.destination?.route
//                when(currentRoute) {
//                    NavDestination.LOGIN.route -> { navController.navigate(NavDestination.BOARD.route) }
//                    NavDestination.BOARD.route -> { navController.navigate(NavDestination.LOGIN.route) }
//                }
//            }) {
//                Text(">>")
//            }
//        }
    )
}

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = NavDestination.LOGIN.route, modifier = modifier) {
        composable(NavDestination.LOGIN.route) { LoginScreen(navController) }
        composable(NavDestination.BOARD.route) { BoardScreen() }
    }
}
