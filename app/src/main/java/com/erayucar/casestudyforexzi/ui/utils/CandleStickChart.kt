package com.erayucar.casestudyforexzi.ui.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.erayucar.casestudyforexzi.core.data.model.CandleEntry
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.cartesian.segmented
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.candlestickSeries
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent


@SuppressLint("RestrictedApi")
@Composable
fun CandleStickChart(
    candleEntries: List<CandleEntry>
) {

    val modelProducer = remember { CartesianChartModelProducer.build() }
    val marker = rememberDefaultCartesianMarker(label = TextComponent.build { Text(text = "") })
    modelProducer.tryRunTransaction {
        candlestickSeries(
            x = candleEntries.map { it.x },
            opening = candleEntries.map { it.open },
            closing = candleEntries.map { it.close },
            low = candleEntries.map { it.low },
            high = candleEntries.map { it.high }

        )

    }
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberCandlestickCartesianLayer(),
            startAxis = rememberStartAxis(),
            bottomAxis =
            rememberBottomAxis(
                guideline = null,
                tick = LineComponent(
                    color = 16053749
                ),
                itemPlacer =
                remember {
                    AxisItemPlacer.Horizontal.default(
                        spacing = 3,
                        addExtremeLabelPadding = true
                    )
                },
            ),
        ),
        modelProducer = modelProducer,
        marker = marker,
        zoomState = rememberVicoZoomState(initialZoom = Zoom.x(8f)),
        modifier = Modifier.fillMaxSize(),
        horizontalLayout = HorizontalLayout.segmented(),
    )

}


