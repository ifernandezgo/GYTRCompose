package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import es.upsa.mimo.gytrcompose.R
import es.upsa.mimo.gytrcompose.network.ExerciseDecoder
import es.upsa.mimo.gytrcompose.previewParameters.ExercisePreviewParameterProvider
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.AddExerciseViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CenterAlignedTopAppBar


private lateinit var addExerciseViewModel: AddExerciseViewModel
private lateinit var onBack: () -> Unit
private var selectedExercise: ExerciseDecoder? = null
private lateinit var onExercise: (String) -> Unit

@Composable
fun AddExercise(
    viewModel: AddExerciseViewModel,
    onBackClicked: () -> Unit,
    onExerciseSelected: (String) -> Unit
) {
    addExerciseViewModel = viewModel
    onBack = onBackClicked
    onExercise = onExerciseSelected
    AddExerciseView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AddExerciseView() {
    val exercises by addExerciseViewModel.getExercises().observeAsState(emptyList())
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "New routine") },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Accent,
                    titleContentColor = White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column {
                SearchBarAddEx()
                DropDownMenusAddEx()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                ) {
                    items(exercises) { exercise ->
                        ExerciseRow(exercise = exercise)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenusAddEx() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var targetExpanded by remember { mutableStateOf(false) }
    var musclesExpanded by remember { mutableStateOf(false) }
    var bodyPartsSelected by remember { mutableStateOf(false) }

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
                                            searchByBodyPartAddEx(bodyPart)
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
                                            searchByMuscleAddEx(muscle)
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
fun SearchBarAddEx() {
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
                onSearchAddEx(text)
            }
            // Hide the keyboard after submitting the search
            keyboardController?.hide()
            //or hide keyboard
            focusManager.clearFocus()
        })
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun ExerciseRow(
    @PreviewParameter(ExercisePreviewParameterProvider::class) exercise: ExerciseDecoder
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .selectable(
            selected = exercise.id === (selectedExercise?.id ?: false),
            onClick = {
                /*selectedExercise = if(exercise.id != (selectedExercise?.id ?: true)) {
                    exercise
                } else {
                    null
                }*/
                onExercise(exercise.id)
            }
        )
    ) {
        GlideImage(
            model = exercise.gifUrl,
            contentDescription = exercise.name,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
                .padding(16.dp)
        )
        Column(modifier = Modifier.fillMaxHeight()) {
            Text(
                text = exercise.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = exercise.target,
                fontSize = 12.sp
            )
        }

    }
}

suspend fun onSearchAddEx(text: String) {
    addExerciseViewModel.getExerciseByName(text)
}

suspend fun searchByBodyPartAddEx(bodyPart: String) {
    addExerciseViewModel.getExerciseByBodyPart(bodyPart = bodyPart)
}

suspend fun searchByMuscleAddEx(muscle: String) {
    addExerciseViewModel.getExerciseByMuscle(muscle = muscle)
}