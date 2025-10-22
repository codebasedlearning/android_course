// (C) 2025 A.Voß, a.voss@fh-aachen.de, info@codebasedlearning.dev

package de.fh_aachen.android.activity_lifecycle.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

interface InputModel {
    val name: MutableState<String>
}

class InputDataModel : InputModel {
    override val name = mutableStateOf("")
}

/*
From Gemini

Creation: When you call
    val myVm: MyViewModel = viewModel()
the framework does create the ViewModel for you, but only if it knows how. Since your InputViewModel
has an empty constructor, it's simple. The framework can create it without any help.
If your ViewModel had constructor parameters (like a repository), you would need to provide
a ViewModelProvider.Factory to explain how to create it.

Scoping: The ViewModel is not one per composable or one per application. It is scoped to
a ViewModelStoreOwner.
    In an Activity: By default, when you call viewModel() inside a composable that is part of
    an Activity, the ViewModel is scoped to that Activity. Every composable within that same
    Activity that asks for the same ViewModel class will receive the exact same instance.
    In Navigation: If you are using Jetpack Navigation, you can also scope a ViewModel to
    a specific navigation graph. This means the ViewModel will be shared by all composables
    that are part of that graph and will be cleared when you navigate away from that graph.

When you rotate the screen, Android destroys and recreates the Activity. However, because
the ViewModel is scoped to the Activity's lifecycle (not its instance), the same ViewModel instance
survives. Your composables get recreated, they ask for the ViewModel again, and the framework
hands them the existing instance, with all its data intact.

So, to summarize:Yes, inheriting from ViewModel is sufficient for the viewModel() function
to provide an instance, as long as it has a no-argument constructor. The key is that you get
a single, shared instance scoped to a lifecycle owner (like an Activity), not a new one for
every composable.
*/

/*
I wanted to use only one implementation, so i replaced this

class InputViewModel : ViewModel() {
    val name = mutableStateOf("")
}

with this class. The important part is ViewModel(), the rest is implementation by delegation.
*/
class InputViewModel : ViewModel(), InputModel by InputDataModel()
