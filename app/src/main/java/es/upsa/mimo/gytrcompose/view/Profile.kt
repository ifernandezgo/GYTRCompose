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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private lateinit var profileViewModel: ProfileViewModel

@Composable
fun Profile(
    viewModel: ProfileViewModel
) {
    profileViewModel = viewModel
    ProfileView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun ProfileView() {
    val histories by profileViewModel.getMyHistories().observeAsState(emptyList())
    var routines by remember { mutableStateOf(mapOf<Int, Routine>()) }
    var series by remember { mutableStateOf(mapOf<Int, List<Exercise>>()) }
    var seriesCounter by remember { mutableStateOf(mapOf<Int, Map<String, Int>>()) }

    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)

    LaunchedEffect(histories) {
        val routinesTmp = mutableMapOf<Int, Routine>()
        val seriesTmp = mutableMapOf<Int, List<Exercise>>()
        val seriesCounterTmp = mutableMapOf<Int, Map<String, Int>>()
        for(history in histories) {
            routinesTmp[history.historyId] = profileViewModel.getRoutineById(history.routineId)!!
            val seriesList = profileViewModel.getSerieByHistoryId(history.historyId)
            val counter = mutableMapOf<String, Int>()
            val exerciseList = mutableListOf<Exercise>()
            for (serie in seriesList) {
                if (counter.contains(serie.exerciseId)) {
                    counter[serie.exerciseId] = counter[serie.exerciseId]!! + 1
                } else {
                    val exercise =
                        profileViewModel.getExerciseById(serie.exerciseId)
                    if (exercise != null) {
                        exerciseList.add(exercise)
                        counter[exercise.exerciseId] = 1
                    }
                }
            }
            seriesTmp[history.historyId] = exerciseList
            seriesCounterTmp[history.historyId] = counter
        }
        routines = routinesTmp
        series = seriesTmp
        seriesCounter = seriesCounterTmp
    }

    Scaffold(
        //Barra superior de la pantalla con el título y color correspondientes
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Profile") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Accent,
                    titleContentColor = White
                )
            )
        },
    ) {
        Box(modifier = Modifier.padding(it).fillMaxSize()) {
            if(histories.isEmpty()) { //No hay entrenaminetos registrados
                //Se muestra un texto en el centro de la pantalla indicándolo
                Text(
                    text = "No trainings registered yet",
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            } else {  //Existen entrenamientos
                //Se muestra toda la lista de entrenamientos
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                ) {
                    items(histories) { history ->
                        //Información general de cada uno de ellos
                        Column {
                            //Nombre de la rutina
                            Text(
                                text = routines[history.historyId]?.name ?: "",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            //Fecha y timepo que duró el entrenamiento en formato fila
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = dateFormatter.format(Date(history.date)).toString(),
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(12.dp)
                                )
                                Text(
                                    text = secondsToHoursMinutesSeconds(history.time),
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(12.dp)
                                )
                            }
                            //Para cada serie del entrenamiento se muestra su información
                            series[history.historyId]?.let { exerciseList ->
                                exerciseList.forEach { exercise ->
                                    //Función que sirve para mostrar la información de cada serie
                                    SetProfile(
                                        exercise = exercise,
                                        counter = seriesCounter[history.historyId] ?: mapOf()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SetProfile(exercise: Exercise, counter: Map<String, Int>) {
    //De cada serie se muestra la imagen del ejercicio y el número de repeticiones
    Row(modifier = Modifier.fillMaxWidth()) {
        GlideImage(
            model = exercise.gifUrl,
            contentDescription = exercise.name,
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
                .padding(16.dp)
        )
        Text(
            modifier = Modifier.fillMaxHeight().align(Alignment.CenterVertically),
            text = counter[exercise.exerciseId].toString() + " series of " + exercise.name
        )
    }
}

private fun secondsToHoursMinutesSeconds(secondsTimer: Int): String {
    val hours = secondsTimer/3600
    val minutes = (secondsTimer % 3600) / 60
    val seconds = (secondsTimer % 3600) % 60
    var time = ""
    if(hours > 0) {
        time += hours.toString() + "h "
    }

    if(minutes > 0) {
        time += minutes.toString() + "min "
    }
    if(seconds != 0) {
        time += seconds.toString() + "s"
    }
    return time
}