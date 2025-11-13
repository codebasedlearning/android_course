// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.models_with_hilt

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
// import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import de.fh_aachen.android.models_with_hilt.ui.composables.BackgroundImage
import de.fh_aachen.android.models_with_hilt.ui.composables.LabeledBox
import de.fh_aachen.android.models_with_hilt.sensors.CityTemperatureViewModel
import de.fh_aachen.android.models_with_hilt.sensors.InjectLegacy
import de.fh_aachen.android.models_with_hilt.sensors.InjectMe
import de.fh_aachen.android.models_with_hilt.sensors.InjectOnce
import de.fh_aachen.android.models_with_hilt.ui.theme.BoxTextColor
import de.fh_aachen.android.models_with_hilt.ui.theme.CityColor
import de.fh_aachen.android.models_with_hilt.ui.theme.MyAppTheme
import javax.inject.Inject

/*
 * @AndroidEntryPoint - Adds a DI container to Android class annotated with it.
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var me: InjectMe           // injected before onCreate
    @Inject lateinit var once: InjectOnce       // (does not work as local var.)
    @Inject lateinit var legacy: InjectLegacy   // mental model: push dependencies in at object construction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(modifier = Modifier.padding(innerPadding),
                        me, once, legacy)
                }
            }
        }
    }
}

@Composable
fun SensorScreen(modifier: Modifier, me:InjectMe, once:InjectOnce, legacy: InjectLegacy) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(id = R.drawable.weather_house)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            Column(modifier = modifier.padding(8.dp)) {
                LabeledBox(fraction = 0.55f, label = "   City", borderColor = CityColor, textColor = BoxTextColor) {
                    Column {
                        CityBox()
                        IconButton(onClick = {
                            me.print()
                            once.print()
                        }, modifier = Modifier) {
                            Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "-1")
                        }
                    }
                }
            }
        }
    }
}

/* instead of
    = viewModel()
 */

@Composable
fun CityBox() {
    val city: CityTemperatureViewModel = hiltViewModel()
    val temperature by city.calibratedValue.collectAsState()
    val bias by city.bias.collectAsState()

    Box(modifier = Modifier.fillMaxWidth().height(40.dp)) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth(0.6f)
            .fillMaxHeight()
            .align(Alignment.CenterStart)
            .background(CityColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "${temperature}°C | $bias",
                modifier = Modifier.padding(end = 8.dp, start = 8.dp),
            )
        }
        Row(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxHeight()
            .align(Alignment.CenterEnd)
            .background(CityColor)
        ) {
            Column(modifier = Modifier.fillMaxHeight().padding(start = 4.dp, end = 4.dp),
                verticalArrangement = Arrangement.Center) {
                IconButton(onClick = { city.calibrate(-1) }, modifier = Modifier.size(20.dp)) {
                    Icon(imageVector = Icons.Filled.ArrowCircleUp, contentDescription = "-1")
                }
                IconButton(onClick = { city.calibrate(+1) }, modifier = Modifier.size(20.dp)) {
                    Icon(imageVector = Icons.Filled.ArrowCircleDown, contentDescription = "+1")
                }
            }
        }
    }
}
