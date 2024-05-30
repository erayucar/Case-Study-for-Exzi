package com.erayucar.casestudyforexzi.ui.orderBook

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.erayucar.casestudyforexzi.R
import com.erayucar.casestudyforexzi.core.data.model.CandleEntry
import com.erayucar.casestudyforexzi.core.network.dto.BookListResponse
import com.erayucar.casestudyforexzi.core.network.dto.Data
import com.erayucar.casestudyforexzi.ui.SharedViewModel
import com.erayucar.casestudyforexzi.ui.theme.Gray80
import com.erayucar.casestudyforexzi.ui.theme.Green80
import com.erayucar.casestudyforexzi.ui.theme.NavyBlue
import com.erayucar.casestudyforexzi.ui.theme.NavyBlue80
import com.erayucar.casestudyforexzi.ui.theme.Red80
import com.erayucar.casestudyforexzi.ui.theme.White80
import com.erayucar.casestudyforexzi.ui.utils.CandleStickChart
import com.erayucar.casestudyforexzi.ui.utils.formatNumberToPercent
import com.erayucar.casestudyforexzi.ui.utils.splitTradingPair
import com.erayucar.casestudyforexzi.ui.utils.toShortenedString
import com.erayucar.casestudyforexzi.ui.utils.toStringFormat
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderBookScreen(
    viewModel: SharedViewModel = hiltViewModel(),
    pairID: String = "1041",
    onBuyClick: (String) -> Unit,
    onBack: () -> Unit
) {

    val orderBookListState = viewModel.orderBookList.observeAsState()
    val graphHistListState = viewModel.graphHistList.observeAsState()
    val pairState = viewModel.pair.observeAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isError = remember {
        mutableStateOf(false)
    }
    val oldRate = remember {
        mutableStateOf(0L)
    }
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.getPair(pairID)
            viewModel.getOrderBook(pairID)
            delay(2000L)
            oldRate.value = pairState.value?.pair?.rate ?: 0
        }

    }
    pairState.value?.pair?.let {
        LaunchedEffect(Unit) {
            viewModel.getGraphHist(pairName = it.name, 1705363200)

        }

    }
    if (pairState.value?.isError != false || graphHistListState.value?.isError != false || orderBookListState.value?.isError != false) {
        isError.value = true
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBlue),
        topBar = {
            pairState.value?.pair?.let {
                Header(it.name.splitTradingPair().first + "/" + it.name.splitTradingPair().second) { onBack() }
            }
        },
        bottomBar = {
            BuySellButtons(

            ) {
                onBuyClick(pairID)
            }
        },
        containerColor = NavyBlue,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            isError.value = false
                        }

                    }
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isError.value) {
                    Dialog(onDismissRequest = { isError.value = false }) {
                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NavyBlue80)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "An error occurred",
                                color = White80,
                                fontSize = 20.sp,
                            )
                        }
                    }
                } else if (pairState.value?.isLoading != false || graphHistListState.value?.isLoading != false || orderBookListState.value?.isLoading != false) {
                    CircularProgressIndicator()
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp), horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = "Chart", color = White80, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Info", color = Gray80, fontSize = 13.sp)
                    }
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFF333C57))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item {
                            pairState.value?.pair?.let {
                                SumSection(screenWidth, it, oldRate.value)

                            }
                        }
                        item {
                            graphHistListState.value?.graphHistList?.let { candleEntries ->
                                ChartSection(candleEntries, screenWidth)
                            }
                        }
                        item {
                            orderBookListState.value?.orderBookList?.let { orderBookList ->
                                pairState.value?.pair?.let { data ->
                                    OrderBookSection(data, orderBookList)
                                }
                            }
                        }
                    }
                }
            }
        })
}

@Composable
fun Header(
    pairName: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyBlue)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = { onBack() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White80)
        }
        Text(
            text = pairName,
            color = Color.White,
            fontSize = 20.sp,
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = { /* Star action */ }) {
                Icon(Icons.Default.Star, contentDescription = "Star", tint = Color.White)
            }
            IconButton(onClick = { /* Share action */ }) {
                Icon(
                    painterResource(R.drawable.share),
                    contentDescription = "Share",
                    tint = White80,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Composable
fun SumSection(
    screenWidth: Dp,
    pair: Data,
    oldRate: Long
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(screenWidth / 2 - 30.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%.2f", pair.rate_f.toFloat()),
                color = if (oldRate > pair.rate) Red80 else if (oldRate == pair.rate) White80 else Green80,
                fontSize = 26.sp
            )

            Row {


                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "=$${pair.main.rate_usd.toStringFormat()}",
                    color = Gray80,
                    fontSize = 13.sp
                )
                Text(
                    text = (if (pair.percent > 0) "+" else "-") + pair.percent.formatNumberToPercent(),
                    color = if (pair.percent > 0) Green80 else Red80,
                    fontSize = 11.sp
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column {
                Column {
                    Text(
                        text = "24h High",
                        color = Gray80,
                        fontSize = 11.sp
                    )
                    Text(
                        text = pair.high_f,
                        color = White80,
                        fontSize = 13.sp
                    )
                }
                Column {
                    Text(
                        text = "24h Amount(${pair.name.splitTradingPair().first})",
                        color = Gray80,
                        fontSize = 11.sp
                    )
                    Text(
                        text = pair.volume_world_f,
                        color = White80,
                        fontSize = 13.sp
                    )
                }
            }
            Column {
                Column(
                ) {
                    Text(
                        text = "24h Low",
                        color = Gray80,
                        fontSize = 11.sp
                    )
                    Text(
                        text = pair.low_f,
                        color = White80,
                        fontSize = 13.sp
                    )
                }
                Column(
                ) {
                    Text(
                        text = "24h Volume(${pair.name.splitTradingPair().second}",
                        color = Gray80,
                        fontSize = 10.sp
                    )
                    Text(
                        text = pair.volume_second_world_f.toInt().toShortenedString(),
                        color = White80,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ChartSection(
    candles: List<CandleEntry>,
    screenWidth: Dp
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .width(screenWidth / 2 - 10.dp)
                .padding(start = 10.dp)
        ) {
            Text("Line", color = Gray80, fontSize = 12.sp)
            Text("15m", color = Gray80, fontSize = 12.sp)
            Text("1h", color = Gray80, fontSize = 12.sp)
            Text("4h", color = Gray80, fontSize = 12.sp)
            Text("1d", color = White80, fontSize = 12.sp)
            Text("1w", color = Gray80, fontSize = 12.sp)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .width(screenWidth / 2 - 10.dp)
                .padding(start = 10.dp)
        ) {
            Text("More⌄", color = Gray80, fontSize = 12.sp)
            Text("Depth", color = Gray80, fontSize = 12.sp)

        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyBlue)
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .height(313.dp)
                .background(NavyBlue80)
        ) {


            CandleStickChart(candleEntries = candles)

        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(116.dp)
                .background(NavyBlue80)
        ) {
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .width(screenWidth / 2 - 10.dp)
                    .padding(start = 10.dp)
            ) {
                Text("MA", color = White80, fontSize = 12.sp)
                Text("EMA", color = Gray80, fontSize = 12.sp)
                Text("BOLL", color = Gray80, fontSize = 12.sp)

            }
            Text(text = "|", color = Gray80, fontSize = 12.sp)
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .width(screenWidth / 2 - 10.dp)
                    .padding(start = 10.dp)
            ) {
                Text("VOL", color = Gray80, fontSize = 12.sp)
                Text("MACD", color = Gray80, fontSize = 12.sp)
                Text("KDJ", color = Gray80, fontSize = 12.sp)
                Text("RSI", color = White80, fontSize = 12.sp)
                Text("WR", color = Gray80, fontSize = 12.sp)

            }
        }
    }
}

@Composable
fun OrderBookSection(
    pair: Data,
    orderBookList: BookListResponse
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = Modifier
            .width(screenWidth)
            .padding(top = 10.dp, start = 10.dp, end = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text("Quantity (${pair.name.splitTradingPair().first})", color = Color.Gray)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Price (${pair.name.splitTradingPair().second}",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),

        ) {
        Column(modifier = Modifier.padding(end = 10.dp)) {
            val buyPercentage = calculateVolumePercentage(orderBookList.buy) { it.volume }
            orderBookList.buy.forEachIndexed() { index, buy ->

                Box(
                    modifier = Modifier
                        .width(screenWidth / 2 - 10.dp)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.CenterEnd

                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = buy.volume_f, color = White80, fontSize = 11.sp)
                        Text(text = buy.rate_f, color = Green80, fontSize = 11.sp)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(buyPercentage[index])
                            .background(Green80.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(text = "")
                    }
                }
            }
        }
        Column {

            val sellPercentage = calculateVolumePercentage(orderBookList.sell) { it.volume }
            orderBookList.sell.forEachIndexed() { index, sell ->

                Box(
                    modifier = Modifier
                        .width(screenWidth / 2 - 10.dp)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.CenterStart

                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = sell.rate_f, color = Red80, fontSize = 11.sp)
                        Text(text = sell.volume_f, color = White80, fontSize = 11.sp)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(sellPercentage[index])
                            .background(Red80.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(text = "")
                    }
                }
            }
        }
    }


}

@Composable
fun BuySellButtons(
    onBuyClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(NavyBlue80)
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.width(100.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    painterResource(id = R.drawable.alert),
                    contentDescription = "alert",
                    tint = White80,
                    modifier = Modifier.size(20.dp)
                )
                Icon(Icons.Default.Star, contentDescription = "alert", tint = NavyBlue80)

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Alert", color = White80, fontSize = 10.sp)

                Text("Margin", color = White80, fontSize = 10.sp)

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                modifier = Modifier
                    .width(118.dp)
                    .height(36.dp),
                onClick = { onBuyClick() },
                colors = ButtonDefaults.buttonColors(containerColor = Green80)
            ) {
                Text("Buy", color = Color.White, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    painterResource(id = R.drawable.buy),
                    contentDescription = "buy",
                    tint = White80
                )


            }
            Button(
                modifier = Modifier
                    .width(118.dp)
                    .height(36.dp),
                onClick = { /* Sell action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Red80)
            ) {
                Text("Sell", color = Color.White, fontSize = 15.sp)
            }
        }

    }
}

fun <T> calculateVolumePercentage(data: List<T>, volumeExtractor: (T) -> Long): List<Float> {
    val totalVolume = data.sumOf(volumeExtractor)
    return data.map { volumeExtractor(it).toFloat() / totalVolume }
}



