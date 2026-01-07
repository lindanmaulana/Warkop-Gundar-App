package com.myproject.warkopgundar.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

fun View.showErrorSnackBar(message: String, anchor: View? = null) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        anchor?.let { anchorView = it }
        setBackgroundTint(resources.getColor(android.R.color.holo_red_dark, null))
        setAction("OK") { }
        show()
    }
}

fun View.showSuccessSnackBar(message: String, anchor: View? = null) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        anchor?.let { anchorView = it }
        setBackgroundTint(resources.getColor(android.R.color.holo_green_dark, null))
        setAction("OK") { }
        show()
    }
}

fun Int.toParseCurrency(): String {
    val localeID = Locale.forLanguageTag("id-ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

    formatRupiah.maximumFractionDigits = 0

    return formatRupiah.format(this.toDouble()).replace("Rp", "Rp ")
}

fun View.gone() { this.visibility = View.GONE }
fun View.visible() { this.visibility = View.VISIBLE }
fun View.invisible() { this.visibility = View.INVISIBLE }