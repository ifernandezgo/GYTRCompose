package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.RoutineViewModel

private lateinit var routineViewModel: RoutineViewModel
private lateinit var onBack: () -> Unit

@Composable
fun Routine(
    viewModel: RoutineViewModel,
    routineId: Int,
    onBackClicked: () -> Unit
) {
    routineViewModel = viewModel
    onBack = onBackClicked
    if(routineId != -1)
        RoutineView(routineId)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoutineView(routineId: Int) {
    var routine by remember { mutableStateOf(Routine()) }
    val exercisesIds by routineViewModel.getRoutineExercises(routineId).observeAsState(emptyList())
    var exercises by remember { mutableStateOf(listOf<Exercise>()) }

    LaunchedEffect(true) {
        val routineTmp = routineViewModel.getRoutineById(routineId)
        if(routineTmp != null) {
            routine = routineTmp
        }
    }

    LaunchedEffect(exercisesIds) {
        if(exercisesIds!!.isNotEmpty()) {
            exercises = routineViewModel.getExercisesById(exercisesIds!!) ?: emptyList()
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

                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
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
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent
                    ),
                    onClick = {

                    }
                ) {
                    Text(text = "Start training")
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(exercises) { exercise ->
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
                    containerColor = Accent
                ),
                onClick = {

                }
            ) {
                Text(text = "Delete routine")
            }
        }
    }
}