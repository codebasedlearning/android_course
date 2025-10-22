// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.models

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fh_aachen.android.models.sensors.CityTemperatureViewModel
import de.fh_aachen.android.models.sensors.RoomTemperatureRefreshingViewModel
import de.fh_aachen.android.models.sensors.RoomTemperatureViewModel
import de.fh_aachen.android.models.sensors.DeviceTemperatureViewModel
import de.fh_aachen.android.models.ui.composables.BackgroundImage
import de.fh_aachen.android.models.ui.composables.LabeledBox
import de.fh_aachen.android.models.ui.theme.BoxTextColor
import de.fh_aachen.android.models.ui.theme.CityColor
import de.fh_aachen.android.models.ui.theme.DeviceColor
import de.fh_aachen.android.models.ui.theme.MyAppTheme
import de.fh_aachen.android.models.ui.theme.RoomColor

const val TAG = "MAIN"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"onCreate, ref $this")

        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SensorScreen(modifier: Modifier) {
    // one way to update a field... not my favorite...
    //    LaunchedEffect(key1 = Unit) {
    //        while (true) {
    //            sensorValue = viewModel.currentValue
    //            delay(1000) // Update every 1000 milliseconds (1 second)
    //        }
    //    }
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(id = R.drawable.weather_house)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            Column(modifier = modifier.padding(8.dp)) {
                LabeledBox(fraction = 0.55f, label = "Room", borderColor = RoomColor, textColor = BoxTextColor) {
                    Column {
                        SensorBox()
                        Spacer(modifier = Modifier.height(4.dp))
                        RoomBox()
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                LabeledBox(fraction = 0.55f, label = "Device", borderColor = DeviceColor, textColor = BoxTextColor) {
                    Column {
                        DeviceBox()
                    }
                }
                LabeledBox(fraction = 0.55f, label = "  City", borderColor = CityColor, textColor = BoxTextColor) {
                    Column {
                        CityBox()
                    }
                }
            }
        }
    }
}

/*
Here the first temperature comes from the repository directly and the raw value is an int.
So we pack it into a state, mutable and of type int, hence a MutableIntState. This can be
observed and if we want to fetch a new value we need to get it manually -> press icon.
The second temperature stems from the state from the viewModel. So this is a state but it is
not updated automatically hence we need to update it manually -> press icon.
*/

@Composable
fun SensorBox() {
    var rawTempRepo by remember { mutableIntStateOf(ModelsApplication.serviceLocator.roomTemperatureRepository.rawValue) }
    val room: RoomTemperatureViewModel = viewModel()
    val rawTempViewModel by room.rawValue // or use room.rawValue.value

    Box(modifier = Modifier.fillMaxWidth().height(40.dp)) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth(0.6f)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(RoomColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "$rawTempRepo | $rawTempViewModel °C (raw)",
                modifier = Modifier.padding(end = 8.dp, start = 8.dp),
            )
        }
        Row(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxHeight()
            .align(Alignment.CenterEnd)
            .background(RoomColor)
        ) {
            IconButton(onClick = {
                // fetch value from repository and update states (UDF: trigger update, observe change)
                ModelsApplication.serviceLocator.roomTemperatureRepository.fetch()
                rawTempRepo = ModelsApplication.serviceLocator.roomTemperatureRepository.rawValue
                room.update()
            }) {
                Icon(imageVector = Icons.Filled.DeviceThermostat, contentDescription = "Update")
            }
        }
    }
}

/*
Here the temperature is the calibrated temperature as state and
it is update every second.
Therefore a new value in the repo needs some time to be seen.
If we change the bias, the viewModel updates internally and that
change takes place immediately.
*/

@Composable
fun RoomBox() {
    val room: RoomTemperatureRefreshingViewModel = viewModel()
    val bias by room.bias
    val calTempViewModel by room.calibratedValue

    Box(modifier = Modifier.fillMaxWidth().height(40.dp)) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth(0.6f)
            .fillMaxHeight()
            .align(Alignment.CenterStart)
            .background(RoomColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "$calTempViewModel (cal.) | $bias °C",
                modifier = Modifier.padding(end = 8.dp, start = 8.dp),
            )
        }
        Row(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxHeight()
            .align(Alignment.CenterEnd)
            .background(RoomColor)
        ) {
            Column(modifier = Modifier.fillMaxHeight().padding(end = 4.dp),
                verticalArrangement = Arrangement.Center) {
                IconButton(onClick = { room.calibrate(+1) }, modifier = Modifier.size(20.dp)) {
                    Icon(imageVector = Icons.Filled.ArrowCircleUp, contentDescription = "+1")
                }
                IconButton(onClick = { room.calibrate(-1) }, modifier = Modifier.size(20.dp)) {
                    Icon(imageVector = Icons.Filled.ArrowCircleDown, contentDescription = "-1")
                }
            }
        }
    }
}

/*
Remember: There two different meaning of 'state'.
A StateFlow does not mean it is observed. Therefore we need a helper
to observe it, that is collectAsState.
*/

@Composable
fun DeviceBox() {
    val device: DeviceTemperatureViewModel = viewModel()
    val temperature by device.calibratedValue.collectAsState() // now observable
    val bias by device.bias.collectAsState()

    Box(modifier = Modifier.fillMaxWidth().height(40.dp)) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth(0.6f)
            .fillMaxHeight()
            .align(Alignment.CenterStart)
            .background(DeviceColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "$temperature (cal.) | $bias °C",
                modifier = Modifier.padding(end = 8.dp, start = 8.dp),
            )
        }
        Row(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxHeight()
            .align(Alignment.CenterEnd)
            .background(DeviceColor)
        ) {
            Column(modifier = Modifier.fillMaxHeight().padding(start = 4.dp, end = 4.dp),
                verticalArrangement = Arrangement.Center) {
                IconButton(onClick = { device.calibrate(-1) }, modifier = Modifier.size(20.dp)) {
                    Icon(imageVector = Icons.Filled.ArrowCircleUp, contentDescription = "-1")
                }
                IconButton(onClick = { device.calibrate(+1) }, modifier = Modifier.size(20.dp)) {
                    Icon(imageVector = Icons.Filled.ArrowCircleDown, contentDescription = "+1")
                }
            }
        }
    }
}

// Similar to DeviceBox

@Composable
fun CityBox() {
    val city: CityTemperatureViewModel = viewModel()
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
            Text(text = "$temperature (cal.) | $bias °C",
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
