package es.upsa.mimo.gytrcompose.view

import android.graphics.drawable.Icon
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.network.ExerciseDecoder
import es.upsa.mimo.gytrcompose.previewParameters.ExercisePreviewParameterProvider
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.NewRoutineViewModel

private lateinit var newRoutineViewModel: NewRoutineViewModel
private lateinit var onBack: () -> Unit

@Composable
fun NewRoutine(viewModel: NewRoutineViewModel, onBackClicked: () -> Unit) {
    newRoutineViewModel = viewModel
    onBack = onBackClicked
    NewRoutineView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NewRoutineView() {
    val exercises: ArrayList<Exercise> = ArrayList()
    var routineName by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "New routine") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Accent,
                    titleContentColor = White
                ),
                actions = {
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
            Column(modifier = Modifier.fillMaxSize()) {
                TextField(
                    value = routineName,
                    onValueChange = { text ->
                        routineName = text
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    maxLines = 1,
                    placeholder = { Text(text = "Routine name") }
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(exercises) { exercise ->
                        ExerciseItem(exercise = exercise)
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    onClick = {
                        //onNewRoutine()
                    }
                ) {
                    Text(text = "Add exercise")
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ExerciseItem(exercise: Exercise) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)) {
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