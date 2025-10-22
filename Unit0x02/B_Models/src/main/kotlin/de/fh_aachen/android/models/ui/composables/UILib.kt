// (C) 2024 A.Voß, a.voss@fh-aachen.de, apps@codebasedlearning.dev

package de.fh_aachen.android.models.ui.composables

import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BackgroundImage(id: Int) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "Background",
        modifier = Modifier.fillMaxSize(),
        // .offset(y = (100).dp)
        // .graphicsLayer(scaleX = 1.2f,scaleY = 1.2f),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun LabeledBox(@FloatRange(from = 0.0, to = 1.0) fraction: Float, label:String, borderColor: Color, textColor: Color,
               content: @Composable () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth(fraction)
        .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
        .padding(8.dp)
    ) {
        Text(text = label, fontSize = 12.sp, color = textColor, modifier = Modifier.size(40.dp)
            .graphicsLayer { rotationZ = -90f } // counterclockwise
            .align(Alignment.CenterStart)
            .offset(x = (0).dp, y = (-32).dp)
        )
        content()
    }
}
