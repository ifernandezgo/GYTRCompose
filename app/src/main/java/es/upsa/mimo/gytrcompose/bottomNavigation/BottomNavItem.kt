package es.upsa.mimo.gytrcompose.bottomNavigation

import es.upsa.mimo.gytrcompose.R

sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
    object Profile: BottomNavItem("Profile", R.drawable.profile, "profile")
    object MyRoutines: BottomNavItem("My routines", R.drawable.myroutines, "myRoutines")
    object Exercises: BottomNavItem("Exercises", R.drawable.exercises, "exercises")
    object Settings: BottomNavItem("Settings", R.drawable.settings, "settings")
}