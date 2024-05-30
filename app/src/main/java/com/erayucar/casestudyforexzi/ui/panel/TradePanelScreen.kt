package com.erayucar.casestudyforexzi.ui.panel

import CustomProgressBar
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erayucar.casestudyforexzi.R
import com.erayucar.casestudyforexzi.core.network.dto.BookListResponse
import com.erayucar.casestudyforexzi.core.network.dto.Data
import com.erayucar.casestudyforexzi.ui.SharedViewModel
import com.erayucar.casestudyforexzi.ui.orderBook.calculateVolumePercentage
import com.erayucar.casestudyforexzi.ui.theme.Gray80
import com.erayucar.casestudyforexzi.ui.theme.Green80
import com.erayucar.casestudyforexzi.ui.theme.NavyBlue
import com.erayucar.casestudyforexzi.ui.theme.NavyBlue90
import com.erayucar.casestudyforexzi.ui.theme.Red80
import com.erayucar.casestudyforexzi.ui.theme.White80
import com.erayucar.casestudyforexzi.ui.utils.formatNumberToPercent
import com.erayucar.casestudyforexzi.ui.utils.toStringFormat
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TradePanelScreen(
    pairID: String,
    viewModel: SharedViewModel = hiltViewModel()
) {

    val orderBookListState = viewModel.orderBookList.observeAsState()
    val pairState = viewModel.pair.observeAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val oldRate = remember {
        mutableStateOf(0L)
    }

    val items = listOf<BottomNavItem>(
        BottomNavItem(
            "EXZI",
            selectedIcon = painterResource(R.drawable.logo),
            unSelectedIcon = painterResource(R.drawable.logo),
            "exzi"
        ),
        BottomNavItem(
            "Market",
            selectedIcon = painterResource(R.drawable.market),
            unSelectedIcon = painterResource(R.drawable.market),
            "market"
        ),
        BottomNavItem(
            "Copy", selectedIcon = painterResource(R.drawable.copy),
            unSelectedIcon = painterResource(R.drawable.copy),
            "copy"
        ),
        BottomNavItem(
            "Assets", selectedIcon = painterResource(R.drawable.assets),
            unSelectedIcon = painterResource(R.drawable.assets),
            "assets"
        )
    )

    LaunchedEffect(Unit) {
        viewModel.getPair(pairID)
        viewModel.getOrderBook(pairID)
        delay(2000L)
        oldRate.value = pairState.value?.pair?.rate ?: 0

    }
    Scaffold(topBar = {

    },
        containerColor = NavyBlue,
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = { /*TODO*/ },
                    shape = CircleShape,
                    containerColor = White80,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .offset(y = 80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.floating_action),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = "Trade", color = White80, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                        .offset(y = 80.dp),
                    fontSize = 12.sp
                )

            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomNavigation(items = items)
        },
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NavyBlue),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (orderBookListState.value?.isLoading != false || pairState.value?.isLoading != false) {
                    CircularProgressIndicator(
                        color = White80,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    pairState.value?.pair?.let {
                        PanelHeader(screenWidth = screenWidth, it)

                    }
                    orderBookListState.value?.orderBookList?.let { bookList ->
                        pairState.value?.pair?.let { data ->
                            Panel(screenWidth, bookList, data)
                        }

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        Text(
                            text = "Open Order",
                            color = White80,
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(
                            text = "Order History",
                            color = Gray80,
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(text = "Funds", color = Gray80, modifier = Modifier.padding(10.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.history),
                            contentDescription = "history",
                            modifier = Modifier
                                .align(
                                    Alignment.CenterVertically
                                )
                                .size(20.dp),
                            tint = White80
                        )

                    }
                    HorizontalDivider(thickness = 0.5.dp, color =Color(0xFF424D70) )
                }

            }


        })

}

@Composable
fun Panel(
    screenWidth: Dp,
    orderBookList: BookListResponse,
    pair: Data
) {
    var tabIndex by remember { mutableStateOf(0) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .width(screenWidth / 2)
                .padding(15.dp)
        ) {

            TabRow(
                modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                selectedTabIndex = tabIndex,
                containerColor = Color(0xFF1B1F2D)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (tabIndex == 0) Green80 else Color.Transparent)
                ) {
                    Tab(
                        modifier = Modifier.background(Color.Transparent),
                        text = { Text("Buy") },
                        selected = tabIndex == 0,
                        onClick = { tabIndex = 0 },
                        selectedContentColor = White80,
                        unselectedContentColor = Gray80,
                    )

                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (tabIndex == 1) Red80 else Color.Transparent)
                ) {
                    Tab(
                        modifier = Modifier.background(Color.Transparent),
                        text = { Text("Sell") },
                        selected = tabIndex == 1,
                        onClick = { tabIndex = 1 },
                        selectedContentColor = White80,
                        unselectedContentColor = Gray80,
                    )

                }

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 15.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, color = Color(0xFF424D70)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Limit", color = White80)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)

                    .padding(top = 15.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, color = Color(0xFF424D70)),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = pair.rate_f,
                    color = White80,
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )
            }

            Text(text = "= ${pair.main.rate_usd.toStringFormat()} USD", color = Gray80, modifier = Modifier.padding(top = 10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)

                    .padding(top = 15.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, color = Color(0xFF424D70)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Amount",
                    color = Gray80,
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )
                Text(
                    text = "BTC",
                    color = Gray80,
                    modifier = Modifier.padding(end = 10.dp, top = 5.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            CustomProgressBar()
            Row(
                modifier = Modifier
                    .fillMaxWidth()


                    .padding(top = 15.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, color = Color(0xFF424D70)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    color = Gray80,
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )
                Text(
                    text = "USDT",
                    color = Gray80,
                    modifier = Modifier.padding(end = 10.dp, top = 5.dp)
                )
            }

            if (tabIndex == 0) {
                Button(
                    onClick = { /*buy action*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .clip(RoundedCornerShape(1.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Green80)
                ) {
                    Text(text = "Buy", color = White80)

                }
            } else {
                Button(
                    onClick = { /*sell action*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .clip(RoundedCornerShape(1.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Red80)

                ) {
                    Text(text = "Sell", color = White80)

                }
            }

        }

        Column(
            modifier = Modifier
                .width(screenWidth / 2 - 10.dp)
                .padding(end = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Price (USDT)",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
                Text("Quantity (BTC)", color = Color.Gray, fontSize = 11.sp)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Column {

                    val sellPercentage =
                        calculateVolumePercentage(orderBookList.sell.subList(0, 5)) { it.volume }
                    orderBookList.sell.subList(0, 5).forEachIndexed { index, sell ->

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.CenterEnd

                        ) {


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = sell.rate_f, color = Red80, fontSize = 11.sp)
                                Text(text = sell.volume_f, color = White80, fontSize = 11.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(sellPercentage[index])
                                    .background(Red80.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(text = "")
                            }
                        }
                    }
                }
                Column {
                    val buyPercentage =
                        calculateVolumePercentage(orderBookList.buy.subList(0, 5)) { it.volume }
                    orderBookList.buy.subList(0, 5).forEachIndexed() { index, buy ->

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.CenterEnd

                        ) {


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = buy.rate_f, color = Green80, fontSize = 11.sp)
                                Text(text = buy.volume_f, color = White80, fontSize = 11.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(buyPercentage[index])
                                    .background(Green80.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(text = "")
                            }
                        }
                    }
                }

            }
        }

    }
}

@Composable
fun BottomNavigation(
    items: List<BottomNavItem>
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),

        containerColor = NavyBlue90,
        tonalElevation = 10.dp,
    ) {

        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }

        items.forEachIndexed { index, it ->
            NavigationRailItem(
                selected = selectedItemIndex == index,
                onClick = {
                    if (selectedItemIndex != index) {
                        selectedItemIndex = index
                    }

                },
                icon = {
                    Image(
                        painter = if (selectedItemIndex == index) it.selectedIcon else it.unSelectedIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = { Text(text = it.title, color = Gray80) },
                alwaysShowLabel = true,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = White80,
                    unselectedIconColor = Gray80,
                    selectedTextColor = White80,
                    unselectedTextColor = Gray80,
                    indicatorColor = Color.Transparent

                ),
                modifier = Modifier.padding(start = 10.dp),

                )
        }

    }
}

@Composable
fun PanelHeader(
    screenWidth: Dp,
    pair: Data
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 20.dp)
    ) {
        Row(
            modifier = Modifier.width(screenWidth / 2),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Spot", color = White80, fontSize = 15.sp)
            Text(text = "Margin", color = Gray80, fontSize = 15.sp)
            Text(text = "Convert", color = Gray80, fontSize = 15.sp)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .width(screenWidth / 2)
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.Bottom

            ) {
                Text(text = pair.name, color = White80, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = (if (pair.percent > 0) "+" else "-") + pair.percent.formatNumberToPercent(),
                    color = if (pair.percent > 0) Green80 else Red80,
                    fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier
                    .width(screenWidth / 2)
                    .padding(top = 20.dp, end = 10.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.market),
                    contentDescription = "Market",
                    modifier = Modifier.size(30.dp),
                    tint = White80
                )
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Market",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 10.dp),
                    tint = White80
                )
            }
        }


    }

}
