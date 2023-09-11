package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.NewRoutineViewModel
import kotlinx.coroutines.launch

private lateinit var newRoutineViewModel: NewRoutineViewModel
private lateinit var onBack: () -> Unit
private lateinit var onAddExercise: () -> Unit
private lateinit var onSave: () -> Unit
private var exercises: ArrayList<Exercise> = ArrayList()
private var exercisesId: MutableList<String> = mutableListOf()
private var routineName: String = ""

@Composable
fun NewRoutine(
    viewModel: NewRoutineViewModel,
    onBackClicked: () -> Unit,
    onAddExerciseClicked: () -> Unit,
    onSaveRoutine: () -> Unit,
    newExercise: String
) {
    newRoutineViewModel = viewModel
    onBack = onBackClicked
    onSave = onSaveRoutine
    onAddExercise = onAddExerciseClicked
    NewRoutineView(newExercise)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Preview
@Composable
private fun NewRoutineView(newExercise: String) {
    var name by remember { mutableStateOf("") }
    if(name != routineName) name = routineName
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var exList by remember { mutableStateOf(listOf<Exercise>()) }

    if(newExercise != "" && !exercisesId.contains(newExercise)) {
        exercisesId.add(newExercise)
        LaunchedEffect(true) {
            val ex = newRoutineViewModel.getExerciseById(newExercise)
            if(ex != null) {
                exercises.add(ex)
                exList = exercises
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "New routine") },
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
                            val saved = saveRoutine(name)
                            if(saved) {
                                exList = listOf()
                                exercises = ArrayList()
                                onSave()
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = null)
                    }
                }
            )
        },
    ) {
        Box(modifier = Modifier
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
                    keyboardActions = KeyboardActions(onDone =  {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    placeholder = { Text(text = "Routine name") }
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(exList) { exercise ->
                        ExerciseFromDb(exercise = exercise)
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

private suspend fun saveRoutine(name: String): Boolean {
    return if(name != "" && newRoutineViewModel.getRoutineByName(name) == null) {
        val routine = Routine(name = name)
        newRoutineViewModel.insertRoutine(routine, exercises)
        true
    } else {
        false
        //val dialog = RoutineExistsDialogFragment()
        //dialog.show(requireActivity().supportFragmentManager, resources.getString(R.string.routine_exists_title))
    }
}