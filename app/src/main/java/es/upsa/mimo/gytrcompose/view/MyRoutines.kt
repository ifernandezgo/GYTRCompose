package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.MyRoutinesViewModel

private lateinit var myRoutinesViewModel: MyRoutinesViewModel

@Composable
fun MyRoutines(viewModel: MyRoutinesViewModel, onNewRoutine: () -> Unit) {
    myRoutinesViewModel = viewModel
    MyRoutinesView(onNewRoutine)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview
fun MyRoutinesView(onNewRoutine: () -> Unit) {
    val routines by myRoutinesViewModel.getRoutines().observeAsState(emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My routines") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Accent,
                    titleContentColor = White
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
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                onClick = {
                    //NewRoutine()
                    onNewRoutine()
                }
            ) {
                Text(text = "New routine")
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(routines) { routine ->
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(12.dp)
                            .align(Alignment.Center),
                        text = routine.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}