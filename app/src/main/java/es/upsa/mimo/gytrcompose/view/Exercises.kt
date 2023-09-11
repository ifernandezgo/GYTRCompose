package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import es.upsa.mimo.gytrcompose.R
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.ExercisesViewModel
import kotlinx.coroutines.launch

private lateinit var exViewModel: ExercisesViewModel

@Composable
fun Exercises(viewModel: ExercisesViewModel) {
    exViewModel = viewModel
    ExercisesView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ExercisesView() {
    val exercises by exViewModel.getExercises().observeAsState(emptyList())
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Exercises") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Accent,
                    titleContentColor = White
                )
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column {
                SearchBar()
                DropDownMenus()
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(exercises) { exercise ->
                        ExerciseFromApi(exercise = exercise)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownMenus() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var targetExpanded by remember { mutableStateOf(false) }
    var musclesExpanded by remember { mutableStateOf(false) }
    var bodyPartsSelected by remember { mutableStateOf(true) }

    val targetTypes = context.resources.getStringArray(R.array.targetTypes)
    val bodyParts = context.resources.getStringArray(R.array.body_parts)
    val muscles = context.resources.getStringArray(R.array.muscles)

    var selectedTargetText by remember { mutableStateOf(targetTypes[0]) }
    var selectedMuscleText by remember { mutableStateOf(bodyParts[0]) }

    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.weight(1f)) {
            ExposedDropdownMenuBox(
                expanded = targetExpanded,
                onExpandedChange = {
                    targetExpanded = !targetExpanded
                }
            ) {
                TextField(
                    value = selectedTargetText,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = targetExpanded
                        )
                    },
                )
                ExposedDropdownMenu(
                    expanded = targetExpanded,
                    onDismissRequest = {
                        targetExpanded = false
                    }
                ) {
                    targetTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(text = type) },
                            onClick = {
                                selectedTargetText = type
                                targetExpanded = false
                                val changed = type == targetTypes[0]
                                if (bodyPartsSelected != changed) {
                                    bodyPartsSelected = changed
                                    selectedMuscleText = if (bodyPartsSelected) {
                                        bodyParts[0]
                                    } else {
                                        muscles[0]
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            ExposedDropdownMenuBox(
                expanded = musclesExpanded,
                onExpandedChange = {
                    musclesExpanded = !musclesExpanded
                }
            ) {
                TextField(
                    value = selectedMuscleText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = musclesExpanded
                        )
                    },
                    modifier = Modifier.menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = musclesExpanded,
                    onDismissRequest = {
                        musclesExpanded = false
                    }
                ) {
                    if (bodyPartsSelected) {
                        //selectedMuscleText = bodyParts[0]
                        bodyParts.forEach { bodyPart ->
                            DropdownMenuItem(
                                text = { Text(text = bodyPart) },
                                onClick = {
                                    selectedMuscleText = bodyPart
                                    musclesExpanded = false
                                    if (bodyPart != bodyParts[0]) {
                                        coroutineScope.launch {
                                            searchByBodyPart(bodyPart)
                                        }
                                    }
                                }
                            )
                        }
                    } else {
                        //selectedMuscleText = muscles[0]
                        muscles.forEach { muscle ->
                            DropdownMenuItem(
                                text = { Text(text = muscle) },
                                onClick = {
                                    selectedMuscleText = muscle
                                    musclesExpanded = false
                                    if (muscle != muscles[0]) {
                                        coroutineScope.launch {
                                            searchByMuscle(muscle)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar() {
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
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
            coroutineScope.launch {
                onSearch(text)
            }
            // Hide the keyboard after submitting the search
            keyboardController?.hide()
            //or hide keyboard
            focusManager.clearFocus()
        })
    )
}

private suspend fun onSearch(text: String) {
    exViewModel.getExerciseByName(text)
}

private suspend fun searchByBodyPart(bodyPart: String) {
    exViewModel.getExerciseByBodyPart(bodyPart = bodyPart)
}

private suspend fun searchByMuscle(muscle: String) {
    exViewModel.getExerciseByMuscle(muscle = muscle)
}