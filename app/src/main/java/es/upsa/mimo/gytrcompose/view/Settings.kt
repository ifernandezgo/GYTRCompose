package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.upsa.mimo.gytrcompose.R
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.AccentDarker
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.ui.theme.standard
import es.upsa.mimo.gytrcompose.viewModel.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Settings() {
    SettingsView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun SettingsView() {
    val settingsViewModel = SettingsViewModel(LocalContext.current)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Settings") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Accent,
                    titleContentColor = White
                )
            )
        },
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                MeasureSettings(settingsViewModel)
                RestTimerSettings(settingsViewModel)
                SetsSettings(settingsViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeasureSettings(settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val weightOptions = context.resources.getStringArray(R.array.weight_settings_options)
    var expanded by remember { mutableStateOf(false) }

    val selectedWeight = settingsViewModel.getWeight.collectAsState(initial = weightOptions[0])

    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        Text(
            text = "Measures",
            fontWeight = FontWeight.Bold,
            color = Accent,
            fontSize = 20.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Weight",
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(6f)
                    .align(Alignment.CenterVertically)
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.weight(4f)
            ) {
                TextField(
                    value = selectedWeight.value,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(textAlign = TextAlign.End)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    weightOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option) },
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    settingsViewModel.saveWeight(option)
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RestTimerSettings(settingsViewModel: SettingsViewModel) {
    val durationOptions = LocalContext.current.resources.getStringArray(R.array.duration_settings_options)
    var expanded by remember { mutableStateOf(false) }
    val selectedDuration = settingsViewModel.getDuration.collectAsState(initial = durationOptions[1])

    val timerEnabled = settingsViewModel.getTimerEnabled.collectAsState(initial = true)

    val notificationEnabled = settingsViewModel.getNotificationEnabled.collectAsState(initial = true)

    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        Text(
            text = "Rest timer",
            fontWeight = FontWeight.Bold,
            color = Accent,
            fontSize = 20.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Rest timer",
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(6f)
                    .align(Alignment.CenterVertically)
            )
            Switch(
                checked = timerEnabled.value,
                onCheckedChange = { enabled ->
                    CoroutineScope(Dispatchers.IO).launch {
                        settingsViewModel.saveTimerEnabled(enabled)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = AccentDarker,
                    checkedThumbColor = White,
                    uncheckedThumbColor = White,
                    uncheckedTrackColor = standard
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Duration",
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(6f)
                    .align(Alignment.CenterVertically)
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.weight(4f)
            ) {
                TextField(
                    value = selectedDuration.value,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(textAlign = TextAlign.End)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    durationOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option) },
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    settingsViewModel.saveDuration(option)
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Rest notication",
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(6f)
                    .align(Alignment.CenterVertically)
            )
            Switch(
                checked = notificationEnabled.value,
                onCheckedChange = { enabled ->
                    CoroutineScope(Dispatchers.IO).launch {
                        settingsViewModel.saveNotificationEnabled(enabled)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = AccentDarker,
                    uncheckedTrackColor = standard
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetsSettings(settingsViewModel: SettingsViewModel) {
    val setsOptions = LocalContext.current.resources.getStringArray(R.array.sets_settings_options)
    var expanded by remember { mutableStateOf(false) }
    val selectedSets = settingsViewModel.getSets.collectAsState(initial = 4)
    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        Text(
            text = "Sets",
            fontWeight = FontWeight.Bold,
            color = Accent,
            fontSize = 20.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Number of sets",
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(6f)
                    .align(Alignment.CenterVertically)
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.weight(4f)
            ) {
                TextField(
                    value = selectedSets.value.toString(),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(textAlign = TextAlign.End)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    setsOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option) },
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    settingsViewModel.saveSets(option.toInt())
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}