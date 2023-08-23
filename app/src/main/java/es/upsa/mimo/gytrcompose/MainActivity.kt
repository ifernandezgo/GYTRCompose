package es.upsa.mimo.gytrcompose

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.upsa.mimo.gytrcompose.bottomNavigation.BottomNavItem
import es.upsa.mimo.gytrcompose.bottomNavigation.BottomNavigation
import es.upsa.mimo.gytrcompose.ui.theme.GYTRComposeTheme
import es.upsa.mimo.gytrcompose.view.AddExercise
import es.upsa.mimo.gytrcompose.view.AddExerciseSelected
import es.upsa.mimo.gytrcompose.view.Exercises
import es.upsa.mimo.gytrcompose.view.MyRoutines
import es.upsa.mimo.gytrcompose.view.NewRoutine
import es.upsa.mimo.gytrcompose.view.Profile
import es.upsa.mimo.gytrcompose.view.Settings
import es.upsa.mimo.gytrcompose.viewModel.AddExerciseViewModel
import es.upsa.mimo.gytrcompose.viewModel.ExercisesViewModel
import es.upsa.mimo.gytrcompose.viewModel.MyRoutinesViewModel
import es.upsa.mimo.gytrcompose.viewModel.NewRoutineViewModel

class MainActivity : ComponentActivity() {

    private val exercisesViewModel by viewModels<ExercisesViewModel>()
    private val myRoutineViewModel by viewModels<MyRoutinesViewModel>()
    private val newRoutineViewModel by viewModels<NewRoutineViewModel>()
    private val addExerciseViewModel by viewModels<AddExerciseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView(exercisesViewModel, myRoutineViewModel, newRoutineViewModel, addExerciseViewModel)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    exercisesViewModel: ExercisesViewModel,
    myRoutinesViewModel: MyRoutinesViewModel,
    newRoutineViewModel: NewRoutineViewModel,
    addExerciseViewModel: AddExerciseViewModel
) {
    val navController = rememberNavController()
    Screen {
        Scaffold(
            bottomBar = {
                BottomNavigation(navController = navController)
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = "profile"
            ) {
                composable(route = BottomNavItem.Profile.screen_route) {
                    Profile()
                }
                composable(route = BottomNavItem.MyRoutines.screen_route) {
                    MyRoutines(
                        myRoutinesViewModel,
                        onNewRoutine = { navController.navigate("newRoutine") }
                    )
                }
                composable(route = BottomNavItem.Exercises.screen_route) {
                    Exercises(exercisesViewModel)
                }
                composable(route = BottomNavItem.Settings.screen_route) {
                    Settings()
                }
                composable(
                    route = "newRoutine?exercisesList={exercisesList}",
                    arguments = listOf(navArgument("exercisesList") { defaultValue = "" })
                ) { navBackStackEntry ->
                    val exercisesArray = navBackStackEntry.arguments?.getString("exercisesList")
                    Log.d("New", exercisesArray.toString())
                    NewRoutine(
                        newRoutineViewModel,
                        onBackClicked = { navController.popBackStack() },
                        onAddExerciseClicked = { ex ->
                            val formattedArray = ex.toList().joinToString(",")
                            Log.d("ListNew", formattedArray)
                            navController.navigate("addExercise?exercisesArray=$formattedArray")
                        },
                        newExercises = exercisesArray?.split(",")
                    )
                }
                composable(
                    route = "addExercise?exercisesArray={exercisesArray}",
                    arguments = listOf(navArgument("exercisesArray") { defaultValue = "" })
                ) { navBackStackEntry ->
                    val exercisesArray = navBackStackEntry.arguments?.getString("exercisesArray")
                    AddExercise(
                        viewModel = addExerciseViewModel,
                        onExerciseSelected = { exSelected, exList ->
                            val formattedArray = exList.toList().joinToString(",")
                            navController.navigate("addExerciseSelected/$exSelected?exercisesArray=$formattedArray")
                        },
                        onBackClicked = { navController.popBackStack() },
                        exList = exercisesArray?.split(",") ?: emptyList()
                    )
                }
                composable(
                    route = "addExerciseSelected/{exerciseSelected}?exercisesArray={exercisesArray}",
                    arguments = listOf(
                        navArgument("exerciseSelected") { type = NavType.StringType },
                        navArgument("exercisesArray") { defaultValue="" }
                    )
                ) { navBackStackEntry ->
                    val exerciseSelected = navBackStackEntry.arguments?.getString("exerciseSelected")
                    val exercisesArray = navBackStackEntry.arguments?.getString("exercisesArray")
                    Log.d("ADEDA", exercisesArray.toString())
                    AddExerciseSelected(
                        viewModel = addExerciseViewModel,
                        exerciseSelected = exerciseSelected ?: "",
                        exList = exercisesArray?.split(",") ?: emptyList(),
                        onBackClicked = { navController.popBackStack() },
                        onAddExercise = { list ->
                            val formattedArray = list.joinToString(",")
                            navController.navigate("newRoutine?exercisesList=$formattedArray")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Screen(content: @Composable () -> Unit) {
    GYTRComposeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}