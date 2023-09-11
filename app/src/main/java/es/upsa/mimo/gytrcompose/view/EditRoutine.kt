package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.EditRoutineViewModel
import kotlinx.coroutines.launch

private lateinit var editRoutineViewModel: EditRoutineViewModel
private lateinit var onBack: () -> Unit
private lateinit var onAddExercise: () -> Unit
private var routineName: String = ""

@Composable
fun EditRoutine(
    viewModel: EditRoutineViewModel,
    routineId: Int,
    onBackClicked: () -> Unit,
    onAddExerciseClicked: () -> Unit
) {
    editRoutineViewModel = viewModel
    onBack = onBackClicked
    onAddExercise = onAddExerciseClicked
    if(routineId != -1)
        EditRoutineView(routineId = routineId)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun EditRoutineView(routineId: Int) {
    var routine by remember { mutableStateOf(Routine()) }
    val exercisesIds by editRoutineViewModel.getRoutineExercises(routineId).observeAsState(emptyList())
    var exercises by remember { mutableStateOf(listOf<Exercise>()) }
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    if(name != routineName) name = routineName
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        val routineTmp = editRoutineViewModel.getRoutineById(routineId)
        if(routineTmp != null) {
            routine = routineTmp
            name = routineTmp.name
            routineName = routineTmp.name
        }
    }

    LaunchedEffect(exercisesIds) {
        if(exercisesIds!!.isNotEmpty()) {
            exercises = editRoutineViewModel.getExercisesById(exercisesIds!!) ?: emptyList()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = routine.name) },
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
                },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val updated = editRoutine(routine, name)
                            if(updated) {
                                onBack()
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                }
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = name,
                    onValueChange = { text ->
                        name = text
                        routineName = text
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    maxLines = 1,
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    placeholder = { Text(text = "Routine name") }
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(exercises) { exercise ->
                        EditRoutineExercise(
                            routineId = routineId,
                            exercise = exercise
                        )
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent,
                    contentColor = White
                ),
                onClick = {
                    onAddExercise()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Add exercise")
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun EditRoutineExercise(
    routineId: Int,
    exercise: Exercise
) {
    val coroutineScope = rememberCoroutineScope()
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)) {
        GlideImage(
            model = exercise.gifUrl,
            contentDescription = exercise.name,
            modifier = Modifier
                .height(90.dp)
                .width(90.dp)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
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
        IconButton(
            modifier = Modifier
                .weight(0.5f),
            onClick = {
                coroutineScope.launch {
                    editRoutineViewModel.deleteExerciseFromRoutine(
                        routineId = routineId,
                        exerciseId = exercise.exerciseId
                    )
                }
            }
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}

private suspend fun editRoutine(routine: Routine, name: String): Boolean {
    val updated: Boolean
    if(name.isEmpty())
    {
        updated = false
    } else {
        if(routine.name != name) {
            if (editRoutineViewModel.getRoutineByName(name) == null) {
                routine.name = name
                editRoutineViewModel.updateRoutine(routine)
                updated = true
            } else {
                updated = false
            }
        } else {
            updated = true
        }
    }
    return updated
}