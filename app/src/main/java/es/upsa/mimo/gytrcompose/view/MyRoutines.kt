package es.upsa.mimo.gytrcompose.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.upsa.mimo.gytrcompose.ui.theme.Accent
import es.upsa.mimo.gytrcompose.ui.theme.White
import es.upsa.mimo.gytrcompose.viewModel.MyRoutinesViewModel

private lateinit var myRoutinesViewModel: MyRoutinesViewModel
private lateinit var onNewRoutine: () -> Unit
private lateinit var onRoutine: (Int) -> Unit

@Composable
fun MyRoutines(
    viewModel: MyRoutinesViewModel,
    onNewRoutineClicked: () -> Unit,
    onRoutineClicked: (Int) -> Unit
) {
    myRoutinesViewModel = viewModel
    onNewRoutine = onNewRoutineClicked
    onRoutine = onRoutineClicked
    MyRoutinesView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview
private fun MyRoutinesView() {
    val routines by myRoutinesViewModel.getRoutines().observeAsState(emptyList())
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "My routines") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Accent,
                    titleContentColor = White
                )
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = White
                    ),
                    onClick = {
                        onNewRoutine()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "New routine")
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    items(routines) { routine ->
                        Text(
                            modifier = Modifier
                                .clickable {
                                    onRoutine(routine.routineId ?: -1)
                                }
                                .fillMaxWidth()
                                .padding(12.dp),
                            text = routine.name,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Divider(modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }
    }
}