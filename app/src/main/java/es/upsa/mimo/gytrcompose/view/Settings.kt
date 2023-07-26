package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.upsa.mimo.gytrcompose.ui.theme.Accent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Accent
                )
                /*actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }*/
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {

        }
    }
}