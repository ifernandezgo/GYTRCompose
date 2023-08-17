package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.ExercisesViewModel

private lateinit var exViewModel: ExercisesViewModel

@Composable
fun Exercises(viewModel: ExercisesViewModel) {
    exViewModel = viewModel;
    //val exercises by exViewModel.getExercises().observeAsState()
    ExercisesView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ExercisesView() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Exercises") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Accent,
                    titleContentColor = White
                )
                /*actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }*/
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column() {
                SearchBar()
                Column {
                    Text(text = "ND")
                    Text(text = "ND")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Search exercise") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent
        ),
        //shape = RoundedCornerShape(4.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(text)
            // Hide the keyboard after submitting the search
            keyboardController?.hide()
            //or hide keyboard
            focusManager.clearFocus()
        })
    )
}

fun onSearch(text: String) {

}