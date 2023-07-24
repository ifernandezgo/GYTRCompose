package es.upsa.mimo.gytrcompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainView() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "GYTR") },
                /*actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }*/
            )
        },
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
                MyRoutines()
            }
            composable(route = BottomNavItem.Exercises.screen_route) {
                MyRoutines()
            }
            composable(route = BottomNavItem.Settings.screen_route) {
                MyRoutines()
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