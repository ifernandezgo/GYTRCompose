package es.upsa.mimo.gytrcompose

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.upsa.mimo.gytrcompose.bottomNavigation.BottomNavItem
import es.upsa.mimo.gytrcompose.bottomNavigation.BottomNavigation
import es.upsa.mimo.gytrcompose.common.Constants
import es.upsa.mimo.gytrcompose.ui.theme.GYTRComposeTheme
import es.upsa.mimo.gytrcompose.view.AddExercise
import es.upsa.mimo.gytrcompose.view.AddExerciseSelected
import es.upsa.mimo.gytrcompose.view.EditRoutine
import es.upsa.mimo.gytrcompose.view.Exercises
import es.upsa.mimo.gytrcompose.view.MyRoutines
import es.upsa.mimo.gytrcompose.view.NewRoutine
import es.upsa.mimo.gytrcompose.view.Profile
import es.upsa.mimo.gytrcompose.view.Routine
import es.upsa.mimo.gytrcompose.view.Settings
import es.upsa.mimo.gytrcompose.view.Training
import es.upsa.mimo.gytrcompose.viewModel.AddExerciseViewModel
import es.upsa.mimo.gytrcompose.viewModel.EditRoutineViewModel
import es.upsa.mimo.gytrcompose.viewModel.ExercisesViewModel
import es.upsa.mimo.gytrcompose.viewModel.MyRoutinesViewModel
import es.upsa.mimo.gytrcompose.viewModel.NewRoutineViewModel
import es.upsa.mimo.gytrcompose.viewModel.ProfileViewModel
import es.upsa.mimo.gytrcompose.viewModel.RoutineViewModel
import es.upsa.mimo.gytrcompose.viewModel.TrainingViewModel

class MainActivity : ComponentActivity() {

    private val profileViewModel by viewModels<ProfileViewModel>()
    private val exercisesViewModel by viewModels<ExercisesViewModel>()
    private val myRoutineViewModel by viewModels<MyRoutinesViewModel>()
    private val newRoutineViewModel by viewModels<NewRoutineViewModel>()
    private val addExerciseViewModel by viewModels<AddExerciseViewModel>()
    private val routineViewModel by viewModels<RoutineViewModel>()
    private val editRoutineViewModel by viewModels<EditRoutineViewModel>()
    private val trainingViewModel by viewModels<TrainingViewModel>()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainView(
                profileViewModel,
                exercisesViewModel,
                myRoutineViewModel,
                newRoutineViewModel,
                addExerciseViewModel,
                routineViewModel,
                editRoutineViewModel,
                trainingViewModel
            )
        }

        createNotificationChannel()

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.restNotificationChannel, "Rest timer", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Channel used for notify when rest time has finished"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainView(
    profileViewModel: ProfileViewModel,
    exercisesViewModel: ExercisesViewModel,
    myRoutinesViewModel: MyRoutinesViewModel,
    newRoutineViewModel: NewRoutineViewModel,
    addExerciseViewModel: AddExerciseViewModel,
    routineViewModel: RoutineViewModel,
    editRoutineViewModel: EditRoutineViewModel,
    trainingViewModel: TrainingViewModel
) {
    val navController = rememberNavController()
    val showBottomAppBar = remember { mutableStateOf(true) }
    val paddingValue = remember { mutableStateOf(60.dp) }

    if(showBottomAppBar.value)
        paddingValue.value = 60.dp
    else
        paddingValue.value = 0.dp

    Screen {
        Scaffold(
            bottomBar = {
                if(showBottomAppBar.value)
                    BottomNavigation(navController = navController)
            },
        ) {
            NavHost(
                navController = navController,
                startDestination = "profile",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValue.value)
            ) {
                composable(route = BottomNavItem.Profile.screen_route) {
                    Profile(
                        profileViewModel
                    )
                }
                composable(route = BottomNavItem.MyRoutines.screen_route) {
                    MyRoutines(
                        myRoutinesViewModel,
                        onNewRoutineClicked = { navController.navigate("newRoutine") },
                        onRoutineClicked = { id -> navController.navigate("routine/$id") }
                    )
                }
                composable(route = BottomNavItem.Exercises.screen_route) {
                    Exercises(exercisesViewModel)
                }
                composable(route = BottomNavItem.Settings.screen_route) {
                    Settings()
                }
                composable(
                    route = "newRoutine?exerciseId={exerciseId}",
                    arguments = listOf(navArgument("exerciseId") { defaultValue = "" })
                ) { navBackStackEntry ->
                    val exerciseId = navBackStackEntry.arguments?.getString("exerciseId")
                    NewRoutine(
                        newRoutineViewModel,
                        onBackClicked = { navController.popBackStack() },
                        onAddExerciseClicked = {
                            navController.navigate("addExercise")
                        },
                        onSaveRoutine = {
                            navController.navigate(BottomNavItem.MyRoutines.screen_route)
                        },
                        newExercise = exerciseId ?: ""
                    )
                }
                composable(
                    route = "addExercise?routineId={routineId}",
                    arguments = listOf(navArgument("routineId") { type = NavType.IntType })
                ) { navBackStackEntry ->
                    val routineId = navBackStackEntry.arguments?.getInt("routineId")
                    AddExercise(
                        viewModel = addExerciseViewModel,
                        onExerciseSelected = { exSelected ->
                            if (routineId == null)
                                navController.navigate("addExerciseSelected/$exSelected")
                            else
                                navController.navigate("addExerciseSelected/$exSelected?routineId=$routineId")
                        },
                        onBackClicked = { navController.popBackStack() }
                    )
                }
                composable(
                    route = "addExerciseSelected/{exerciseSelected}?routineId={routineId}",
                    arguments = listOf(
                        navArgument("exerciseSelected") { type = NavType.StringType },
                        navArgument("routineId") { type = NavType.IntType }
                    )
                ) { navBackStackEntry ->
                    val exerciseSelected =
                        navBackStackEntry.arguments?.getString("exerciseSelected")
                    val routineId = navBackStackEntry.arguments?.getInt("routineId")
                    AddExerciseSelected(
                        viewModel = addExerciseViewModel,
                        exerciseSelected = exerciseSelected ?: "",
                        routineId = routineId,
                        onBackClicked = { navController.popBackStack() },
                        onAddExercise = { exercise ->
                            if (routineId == null)
                                navController.navigate("newRoutine?exerciseId=$exercise")
                            else
                                navController.navigate("editRoutine/$routineId")
                        }
                    )
                }
                composable(
                    route = "routine/{routineId}",
                    arguments = listOf(navArgument("routineId") { type = NavType.IntType })
                ) { navBackStackEntry ->
                    val routineId = navBackStackEntry.arguments?.getInt("routineId")
                    Routine(
                        viewModel = routineViewModel,
                        routineId = routineId ?: -1,
                        onBackClicked = { navController.navigate(BottomNavItem.MyRoutines.screen_route) },
                        onEditRoutineClicked = { id -> navController.navigate("editRoutine/$id") },
                        onStartTrainingClicked = { id ->
                            showBottomAppBar.value = false
                            navController.navigate("training/$id")
                        }
                    )
                }
                composable(
                    route = "editRoutine/{routineId}",
                    arguments = listOf(navArgument("routineId") { type = NavType.IntType })
                ) { navBackStackEntry ->
                    val routineId = navBackStackEntry.arguments?.getInt("routineId")
                    EditRoutine(
                        viewModel = editRoutineViewModel,
                        routineId = routineId ?: -1,
                        onBackClicked = { navController.navigate("routine/$routineId") },
                        onAddExerciseClicked = { navController.navigate("addExercise?routineId=$routineId") }
                    )
                }
                composable(
                    route = "training/{routineId}",
                    arguments = listOf(navArgument("routineId") { type = NavType.IntType })
                ) { navBackStackEntry ->
                    val routineId = navBackStackEntry.arguments?.getInt("routineId")
                    Training(
                        viewModel = trainingViewModel,
                        routineId = routineId ?: -1,
                        onFinishedClicked = {
                            showBottomAppBar.value = true
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Screen(content: @Composable () -> Unit) {
    GYTRComposeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}