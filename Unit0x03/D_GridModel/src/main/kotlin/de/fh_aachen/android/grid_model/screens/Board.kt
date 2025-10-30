// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.grid_model.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.fh_aachen.android.grid_model.GridModelApplication

@Composable
fun BoardScreen() {
    val model = GridModelApplication.appModel

    Box(modifier = Modifier.fillMaxSize()) {
        NavDestination.BOARD.Image
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(model.dim),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(model.clicks.size) { index ->
                    CardComposable(index)
                }
            }
        }
    }
}

@Composable
fun CardComposable(index: Int) {
    val model = GridModelApplication.appModel
    Card(
        modifier = Modifier.size(150.dp),
        onClick = { model.isClicked(index) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
            // contentAlignment = Alignment.Center
        ) {
            model.cardImageOf(index)
            Image(
                painter = painterResource(id = model.cardImageOf(index)),
                contentDescription = "Background",
                //modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Box(
                modifier = Modifier.padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                val text = model.cardTextOf(index)
                if (text.isNotEmpty()) {
                    RoundedRectangleWithText(text = text)
                }
            }
        }
    }
}
