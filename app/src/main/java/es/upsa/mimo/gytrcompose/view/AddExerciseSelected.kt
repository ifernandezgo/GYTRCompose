package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import es.upsa.mimo.gytrcompose.network.ExerciseDecoder
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White

private lateinit var exercise: ExerciseDecoder
private lateinit var onBack: () -> Unit

@Composable
fun AddExerciseSelected(
    exerciseSelected: ExerciseDecoder,
    onBackClicked: () -> Unit
) {
    exercise = exerciseSelected
    onBack = onBackClicked
    AddExerciseSelectedView()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddExerciseSelectedView() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = exercise.name) },
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
            Column {
                GlideImage(
                    model = exercise.gifUrl,
                    contentDescription = exercise.name,
                    modifier = Modifier
                        .height(120.dp)
                        .width(1200.dp)
                        .padding(16.dp)
                )
                Text(text = exercise.name)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    onClick = {
                        //onAddExercise()
                    }
                ) {
                    Text(text = "Add exercise")
                }
            }

        }
    }
}