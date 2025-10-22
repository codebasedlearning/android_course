// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.grid_model.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavDestination.LOGIN.Image
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { navController.navigate(NavDestination.BOARD.route) }) {
                Text("Login...", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
