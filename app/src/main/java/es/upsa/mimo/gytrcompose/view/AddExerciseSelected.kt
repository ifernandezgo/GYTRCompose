package es.upsa.mimo.gytrcompose.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.AddExerciseViewModel
import kotlinx.coroutines.launch

private lateinit var addExerciseViewModel: AddExerciseViewModel
private lateinit var exerciseId: String
private var routine: Int? = null
private lateinit var onBack: () -> Unit
private lateinit var onAdd: (String) -> Unit

@Composable
fun AddExerciseSelected(
    viewModel: AddExerciseViewModel,
    exerciseSelected: String,
    routineId: Int?,
    onBackClicked: () -> Unit,
    onAddExercise: (String) -> Unit
) {
    addExerciseViewModel = viewModel
    onBack = onBackClicked
    onAdd = onAddExercise
    exerciseId = exerciseSelected
    routine = routineId
    LaunchedEffect(true) {
        addExerciseViewModel.getExerciseByIdApi(exerciseId)
    }
    Log.d("AddExSelected", exerciseId)
    AddExerciseSelectedView()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
private fun AddExerciseSelectedView() {
    val exercise by addExerciseViewModel.getExercise().observeAsState()
    val coroutineScope = rememberCoroutineScope()
    if(exercise == null) {
        Text(text = "Loading")
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = exercise!!.name) },
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
            Box(modifier = Modifier
                .padding(it)
                .fillMaxSize()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    GlideImage(
                        model = exercise!!.gifUrl,
                        contentDescription = exercise!!.name,
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Text(
                        text = "Body part:",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp, top = 16.dp)
                    )
                    Text(
                        text = exercise!!.bodyPart,
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp)
                    )
                    Text(
                        text = "Target:",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp)
                    )
                    Text(
                        text = exercise!!.target,
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp)
                    )
                    Text(
                        text = "Equipment:",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp)
                    )
                    Text(
                        text = exercise!!.equipment,
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp)
                    )
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
                        val exerciseDb = Exercise(
                            exerciseId = exercise!!.id,
                            name = exercise!!.name,
                            target = exercise!!.target,
                            bodyPart = exercise!!.bodyPart,
                            equipment = exercise!!.equipment,
                            gifUrl = exercise!!.gifUrl
                        )
                        coroutineScope.launch {
                            if(addExerciseViewModel.getExerciseById(exerciseDb.exerciseId) == null)
                                addExerciseViewModel.insertExercise(exerciseDb)
                            if(routine != -1)
                                addExerciseViewModel.addExerciseToRoutine(routine!!, exerciseDb.exerciseId)
                        }
                        onAdd(exerciseId)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Add exercise")
                }
            }
        }
    }
}