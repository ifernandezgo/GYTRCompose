package es.upsa.mimo.gytrcompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.upsa.mimo.gytrcompose.bottomNavigation.BottomNavItem
import es.upsa.mimo.gytrcompose.bottomNavigation.BottomNavigation
import es.upsa.mimo.gytrcompose.ui.theme.GYTRComposeTheme
import es.upsa.mimo.gytrcompose.view.Exercises
import es.upsa.mimo.gytrcompose.view.MyRoutines
import es.upsa.mimo.gytrcompose.view.Profile
import es.upsa.mimo.gytrcompose.view.Settings
import es.upsa.mimo.gytrcompose.viewModel.ExercisesViewModel
import es.upsa.mimo.gytrcompose.viewModel.MyRoutinesViewModel

class MainActivity : ComponentActivity() {

    private val exercisesViewModel by viewModels<ExercisesViewModel>()
    private val myRoutineViewModel by viewModels<MyRoutinesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView(exercisesViewModel, myRoutineViewModel)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(exercisesViewModel: ExercisesViewModel, myRoutinesViewModel: MyRoutinesViewModel) {
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
                    MyRoutines(myRoutinesViewModel)
                }
                composable(route = BottomNavItem.Exercises.screen_route) {
                    Exercises(exercisesViewModel)
                }
                composable(route = BottomNavItem.Settings.screen_route) {
                    Settings()
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