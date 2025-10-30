// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.grid_model.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import de.fh_aachen.android.grid_model.R

enum class NavDestination(val route: String, val doc: String,val icon: ImageVector, val resId:Int) {
    LOGIN("login", "Login", Icons.Default.Person, R.drawable.login),
    BOARD("board", "Board", Icons.Default.PlayArrow, R.drawable.board);

    val Icon: Unit
        @Composable
        get() = Icon(imageVector = this.icon, contentDescription = this.doc)

    val Image: Unit
        @Composable
        get() = Image(
            // bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, this.resId).asImageBitmap(),
            painter = painterResource(id = this.resId),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // This scales the image to fill the entire box
        )
}
