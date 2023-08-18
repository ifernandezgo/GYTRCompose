package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import es.upsa.mimo.gytrcompose.network.ExerciseDecoder
import es.upsa.mimo.gytrcompose.previewParameters.ExercisePreviewParameterProvider

@Preview
@Composable
fun Exercise(@PreviewParameter(ExercisePreviewParameterProvider::class) exercise: ExerciseDecoder) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = exercise.name)

    }
}