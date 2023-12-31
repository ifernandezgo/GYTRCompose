package es.upsa.mimo.gytrcompose.view

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.Serie
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.TrainingViewModel
import es.upsa.mimo.gytrcompose.model.Routine
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import es.upsa.mimo.gytrcompose.R
import es.upsa.mimo.gytrcompose.common.Constants
import es.upsa.mimo.gytrcompose.model.History
import es.upsa.mimo.gytrcompose.ui.theme.completed
import es.upsa.mimo.gytrcompose.viewModel.SettingsViewModel
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

private lateinit var trainingViewModel: TrainingViewModel
private lateinit var onFinish: () -> Unit
private lateinit var timer: Timer
private var restTimer: CountDownTimer? = null
private var numberOfSets: Int = 4
private var durationRestTimer = 60f
private var seriesCompleted = ArrayList<Serie>()

@Composable
fun Training(
    viewModel: TrainingViewModel,
    routineId: Int,
    onFinishedClicked: () -> Unit
) {
    trainingViewModel = viewModel
    onFinish = onFinishedClicked
    timer = Timer()

    val settingsViewModel = SettingsViewModel(LocalContext.current)
    numberOfSets = settingsViewModel.getSets.collectAsState(initial = 4).value
    durationRestTimer = getRestTimerDuration(settingsViewModel.getDuration.collectAsState(initial = "1min").value)

    Log.d("ROUTINE ID", routineId.toString())
    if(routineId != -1)
        TrainingView(routineId, settingsViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrainingView(routineId: Int, settingsViewModel: SettingsViewModel) {
    var routine by remember { mutableStateOf(Routine()) }
    val exercisesIds by trainingViewModel.getRoutineExercises(routineId).observeAsState(emptyList())
    var exercises by remember { mutableStateOf(listOf<Exercise>()) }

    val coroutineScope = rememberCoroutineScope()

    val restTimerCount = remember { mutableFloatStateOf(0f) }

    var durationTimer by remember { mutableIntStateOf(0) }
    var durationText by remember { mutableStateOf("") }
    val setsCompleted = remember { mutableIntStateOf(0) }

    var showSaveDialog by remember { mutableStateOf(false) }

    val restTimerEnabled = settingsViewModel.getTimerEnabled.collectAsState(initial = true)

    LaunchedEffect(true) {
        val routineTmp = trainingViewModel.getRoutineById(routineId)
        if(routineTmp != null) {
            routine = routineTmp
        }
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                durationTimer += 1
                durationText = secondsToHoursMinutesSeconds(durationTimer)
            }
        }, 0, 1000)
    }

    LaunchedEffect(exercisesIds) {
        if(exercisesIds!!.isNotEmpty()) {
            exercises = trainingViewModel.getExercisesById(exercisesIds!!) ?: emptyList()
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
                actions = {
                    IconButton(onClick = {
                        timer.cancel()
                        restTimer?.cancel()
                        showSaveDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = null)
                    }
                }
            )
        },
    ) {
        Box(modifier = Modifier.padding(it).fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Header(durationText = durationText, setsCompleted)
                if(restTimerEnabled.value) {
                    LinearProgressIndicator(
                        progress = restTimerCount.floatValue / durationRestTimer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = Accent
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(exercises) { exercise ->
                        TrainingExercise(
                            exercise = exercise,
                            setsCompleted = setsCompleted,
                            restTimerCount = restTimerCount,
                            settingsViewModel = settingsViewModel
                        )
                    }
                }
            }

            SaveTrainingDialog(
                showSaveDialog = showSaveDialog,
                onDismiss = {
                    showSaveDialog = false
                    onFinish()
                },
                onSave = {
                    showSaveDialog = false
                    val history = History(time = durationTimer, routineId = routineId, historyId = 0)
                    coroutineScope.launch {
                        saveTraining(history)
                        seriesCompleted = ArrayList()
                    }
                    onFinish()
                }
            )
        }
    }
}

@Composable
private fun Header(durationText: String, setsCompleted: MutableState<Int>) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .weight(4f)
        ) {
            Text(
                text = "Duration",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = durationText,
                fontSize = 12.sp
            )
        }
        Column(
            modifier = Modifier
                .padding(12.dp)
                .weight(10f)
        ) {
            Text(
                text = "Sets",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = setsCompleted.value.toString(),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TrainingExercise(
    exercise: Exercise,
    setsCompleted: MutableState<Int>,
    restTimerCount: MutableState<Float>,
    settingsViewModel: SettingsViewModel
) {
    Column {
        ExerciseHeader(exercise = exercise)
        SetsHeader()
        for(i in 1..numberOfSets) {
            ExerciseSets(exercise, i, setsCompleted, restTimerCount, settingsViewModel)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ExerciseHeader(exercise: Exercise) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        GlideImage(
            model = exercise.gifUrl,
            contentDescription = exercise.name,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
                .padding(16.dp)
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = exercise.name,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

    }
}

@Composable
@Preview
private fun SetsHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.weight(10f),
            text = "Sets",
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(10f),
            text = "Last rep",
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .weight(10f),
            text = "Weight",
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(10f),
            text = "Reps",
            textAlign = TextAlign.Center
        )
        Icon(
            modifier = Modifier.weight(10f),
            imageVector = Icons.Default.Check,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun ExerciseSets(
    exercise: Exercise,
    numberOfSet: Int,
    setsCompleted: MutableState<Int>,
    restTimerCount: MutableState<Float>,
    settingsViewModel: SettingsViewModel
) {
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var finished by remember { mutableStateOf(false) }
    var series by remember { mutableStateOf(listOf<Serie>()) }

    LaunchedEffect(true) {
        series = trainingViewModel.getExerciseLastSeries(exercise.exerciseId, numberOfSets)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val background = if(finished) completed else MaterialTheme.colorScheme.background

    val restTimerEnabled = settingsViewModel.getTimerEnabled.collectAsState(initial = true)
    val notificationEnabled = settingsViewModel.getNotificationEnabled.collectAsState(initial = true)

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .background(background)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(10f),
            text = numberOfSet.toString(),
            textAlign = TextAlign.Center
        )
        if (numberOfSet-1 < series.size) {
            Text(
                modifier = Modifier.weight(10f),
                text = series[numberOfSet-1].weight.toString() + "x" + series[numberOfSet-1].repetitions.toString(),
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                modifier = Modifier.weight(10f),
                text = "-",
                textAlign = TextAlign.Center
            )
        }
        TextField(
            modifier = Modifier
                .weight(10f)
                .height(50.dp),
            value = weight,
            onValueChange = { text ->
                weight = text
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone =  {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.small,
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "0",
                    textAlign = TextAlign.Center
                )
            },
            enabled = !finished,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
        TextField(
            modifier = Modifier
                .weight(10f)
                .height(50.dp),
            value = reps,
            onValueChange = { text ->
                reps = text
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone =  {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.small,
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "0",
                    textAlign = TextAlign.Center
                )
            },
            enabled = !finished,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
        IconButton(
            modifier = Modifier.weight(10f),
            onClick = {
                finished = true
                if(reps == "") reps = "0"
                if(weight == "") weight = "0"
                val serie = Serie(repetitions = reps.toInt(), weight = weight.toFloat(), exerciseId = exercise.exerciseId)
                seriesCompleted.add(serie)
                setsCompleted.value += 1
                if(restTimerEnabled.value) {
                    restTimerCount.value = 0f
                    restTimer?.cancel()
                    restTimer = object : CountDownTimer((durationRestTimer * 1000).toLong(), 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            restTimerCount.value += 1
                        }

                        override fun onFinish() {
                            restTimerCount.value = 0f
                            if(notificationEnabled.value) {
                                if (ActivityCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    val notification =
                                        NotificationCompat.Builder(context, Constants.restNotificationChannel)
                                            .setSmallIcon(R.drawable.icon)
                                            .setContentTitle("Rest time")
                                            .setContentText("Rest time has finished!")
                                            .setSound(Uri.parse("android.resource://"+context.packageName+"/"+R.raw.whistle))
                                            .build()
                                    val notificationManager =
                                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                    notificationManager.notify(Constants.restNotificationId, notification)
                                }
                            }
                        }
                    }.start()
                }
            },
            enabled = !finished
        ) {
            Icon(
                modifier = Modifier.weight(10f),
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SaveTrainingDialog(
    showSaveDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    if(showSaveDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Save training") },
            text = { Text(text = "Do you really want to save this training?") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = White
                    ),
                    onClick = {
                        onSave()
                    }
                ) {
                    Text(text = "Save")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = White
                    ),
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
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

    time += seconds.toString() + "s"
    return time
}

private suspend fun saveTraining(history: History) {
    val historyId = trainingViewModel.insertHistory(history)
    val seriesList = seriesCompleted.toList()
    for(newSerie in seriesList) {
        newSerie.historyId = historyId.toInt()
    }
    trainingViewModel.insertSeries(seriesList)
}

private fun getRestTimerDuration(duration: String): Float {
    return when(duration) {
        "30s" -> 30f
        "1min" -> 60f
        "1min 30s" -> 90f
        "2min" -> 120f
        "2min 30s" -> 150f
        "3min" -> 180f
        "3min 30s" -> 210f
        "4min" -> 240f
        "4min 30s" -> 270f
        "5min" -> 300f
        else -> 60f
    }
}