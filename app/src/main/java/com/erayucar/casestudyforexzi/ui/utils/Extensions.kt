package com.erayucar.casestudyforexzi.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.Locale

fun Double.formatNumberToPercent(): String {
    // Divide the number by 100000 to get the desired value
    val adjustedNumber = this / 1000.0

    // Define the format pattern
    val decimalFormat = DecimalFormat("#,##0.00%")

    // Format the adjusted number
    return decimalFormat.format(adjustedNumber)
}

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

fun ChartRect.contains(x: Float, y: Float): Boolean {
    return x >= this.left && x <= this.right && y >= this.top && y <= this.bottom
}

fun Long.toStringFormat(): String {
    val rate_usdLong =BigDecimal(this / 1000000).movePointLeft(2)
   return String.format(Locale.US, "%,.2f", rate_usdLong)
}