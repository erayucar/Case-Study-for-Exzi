package com.erayucar.casestudyforexzi.ui.utils

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

fun ChartRect.contains(x: Float, y: Float): Boolean {
    return x >= this.left && x <= this.right && y >= this.top && y <= this.bottom
}

fun Long.toStringFormat(): String {
    val rate_usdLong =BigDecimal(this / 1000000).movePointLeft(2)
   return String.format(Locale.US, "%,.2f", rate_usdLong)
}

fun String.splitTradingPair(): Pair<String, String> {
    // Define a set of known currency codes
    val knownCurrencies = setOf("BTC", "ETH", "USDT","LTC","OMG","BCH","ENJ","TRX","XLM","MATIC","LINK","TRY","AVAX","MANA","FTM", "TRY","EOS","ATOM","SAND","GRT","AXS","ALGO","CHZ","SHIB","COTI", "BNB", "XRP", "ADA", "DOGE", "DOT", "SOL")

    // Iterate over the known currencies to find the base currency
    for (currency in knownCurrencies) {
        if (this.startsWith(currency)) {
            val baseCurrency = currency
            val quoteCurrency = this.removePrefix(currency)
            if (quoteCurrency in knownCurrencies) {
                return Pair(baseCurrency, quoteCurrency)
            }
        }
    }

    throw IllegalArgumentException("Invalid trading pair format: $this")
}
fun Int.toShortenedString(): String {
    return when {
        this >= 1_000_000_000 -> String.format(Locale.US, "%.3fB", this / 1_000_000_000.0)
        this >= 1_000_000 -> String.format(Locale.US, "%.3fM", this / 1_000_000.0)
        this >= 1_000 -> String.format(Locale.US, "%.3fK", this / 1_000.0)
        else -> this.toString()
    }.removeSuffix(".000")  // Remove trailing zeros if any
}