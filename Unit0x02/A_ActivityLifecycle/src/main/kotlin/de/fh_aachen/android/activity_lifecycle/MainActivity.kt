// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.activity_lifecycle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.fh_aachen.android.activity_lifecycle.ui.theme.MyAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fh_aachen.android.activity_lifecycle.models.InputDataModel
import de.fh_aachen.android.activity_lifecycle.models.InputViewModel

const val TAG = "MAIN"

class MainActivity : ComponentActivity() {
    val inputModel = InputDataModel()   // only needed for InputVersion3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"onCreate, ref $this")

        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InputBox(inputModel = inputModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG,"onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG,"onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"onDestroy, ref $this")
    }
}

@Composable
fun InputBox(inputModel: InputDataModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Image(
            // bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, this.resId).asImageBitmap(),
            painter = painterResource(id = R.drawable.library_small),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // This scales the image to fill the entire box
        )

        Box(modifier = Modifier.background(color = White).padding(all = 8.dp)) {
            Column {
                Text(
                    "Enter names, rotate the device and watch the inputs and the LogCat.",
                    modifier = Modifier.width(300.dp)
                )

                /*
                 * We use different ways to store text. The core idea is to consider the lifetime
                 * of the object. How long should the text remain valid?
                 */

                Spacer(modifier = Modifier.height(16.dp))
                InputVersion1()
                Spacer(modifier = Modifier.height(16.dp))
                InputVersion2()

                Spacer(modifier = Modifier.height(16.dp))
                InputVersion3(inputModel = inputModel) // here we need the model

                Spacer(modifier = Modifier.height(16.dp))
                InputVersion4()

                Spacer(modifier = Modifier.height(16.dp))
                InputVersion5()
            }
        }
    }
}

// 'remember' essentially acts as an in-memory cache within the composable lifecycle,
//  it does not survive configuration changes like screen rotation
@Composable
fun InputVersion1() {
    var name by remember { mutableStateOf("") }
    InputBlock(version = "Version 1", value = name, onValueChange = { name = it })
}

// 'rememberSaveable' is similar to remember but can store values in the saved instance state,
// preserving them across configuration changes
@Composable
fun InputVersion2() {
    var name by rememberSaveable { mutableStateOf("") }
    InputBlock(version = "Version 2", value = name, onValueChange = { name = it })
}

// inputModel is injected into the composable function,
// here it depends on the lifecycle of the calling activity instance
@Composable
fun InputVersion3(inputModel: InputDataModel) {
    InputBlock(version = "Version 3", value = inputModel.name.value, onValueChange = { inputModel.name.value = it })
}

// here the used inputModel depends on the application lifecycle, see Application
@Composable
fun InputVersion4() {
    val inputModel = ActivityLifecycleApplication.instance.inputModel
    InputBlock(version = "Version 4", value = inputModel.name.value, onValueChange = { inputModel.name.value = it })
}

@Composable
fun InputVersion5() {
    val inputModel: InputViewModel = viewModel()
    InputBlock(version = "Version 5", value = inputModel.name.value, onValueChange = { inputModel.name.value = it })
}

/* One version, but not optimal - why?

@Composable
fun InputBlock(version:String, name: MutableState<String>) {
    Column {
        Text("$version, name '${name.value}'")
        TextField(
            value = name.value,
            onValueChange = { newText -> name.value = newText },
            label = { Text("Enter name") },
            modifier = Modifier.width(300.dp),
        )
    }
}
*/

@Composable
fun InputBlock(version: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text("$version, name '$value'")
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Enter name") },
            modifier = Modifier.width(300.dp),
        )
    }
}
