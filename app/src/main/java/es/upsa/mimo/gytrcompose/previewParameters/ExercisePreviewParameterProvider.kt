package es.upsa.mimo.gytrcompose.previewParameters

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import es.upsa.mimo.gytrcompose.network.ExerciseDecoder

class ExercisePreviewParameterProvider: PreviewParameterProvider<ExerciseDecoder> {
    override val values = sequenceOf(
        ExerciseDecoder(id = "1", name = "Name", bodyPart = "Body part", equipment = "Equipment", gifUrl = "", target = "Target")
    )
}