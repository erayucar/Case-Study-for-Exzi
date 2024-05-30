package com.erayucar.casestudyforexzi.ui.pair

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.erayucar.casestudyforexzi.core.network.dto.Data
import com.erayucar.casestudyforexzi.ui.theme.NavyBlue
import com.erayucar.casestudyforexzi.ui.theme.White80

@Composable
fun PairListScreen(
    viewModel: PairListViewModel = hiltViewModel(),
    onPairClick: (Data) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getPairList()
    }
    val state = viewModel.pairList.observeAsState()
    var filterText by remember { mutableStateOf(TextFieldValue("")) }
    val isError = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.value?.isError) {
        isError.value = state.value?.isError ?: false
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBlue),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.value?.isError != false) {
            Dialog(onDismissRequest = { isError.value = false }) {
                Text(
                    text = "An error occurred",
                    color = Color.Red,
                    fontSize = 20.sp,
                )
            }
        }
        if (state.value?.isLoading != false) {
            CircularProgressIndicator()
        } else {
            TextField(
                value = filterText,
                onValueChange = { newText ->
                    filterText = newText
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Filter pairs") }
            )

            val filteredPairs = state.value?.pairList?.filter { pair ->
                pair.name.contains(filterText.text, ignoreCase = true)
            } ?: emptyList()
            PairListContent(pairList = filteredPairs) {
                onPairClick(it)
            }
        }
    }


}

@Composable
fun PairListContent(pairList: List<Data>, onClick: (Data) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(pairList) { pair ->
            PairListItem(pair = pair) {
                onClick(pair)
            }

            HorizontalDivider(thickness = 0.6.dp, color = Color.Gray)
        }
    }
}

@Composable
fun PairListItem(pair: Data, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Text(text = pair.name, color = White80, modifier = Modifier.padding(30.dp))
    }
}
