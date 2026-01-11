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

fun Int?.toLikeCountFormat(): String {
    val value = this ?: 0

    val localeID = Locale.forLanguageTag("id-ID")
    val formatter = NumberFormat.getNumberInstance(localeID)

    return "(${formatter.format(value)})"
}

fun maskEmail(email: String?): String {
    if (email.isNullOrEmpty() || !email.contains("@")) return email ?: ""

    val parts = email.split("@")
    val name = parts[0]
    val domain = parts[1]

    return when {
        name.length >= 5 -> {
            "${name.take(2)}****${name.takeLast(2)}@$domain"
        }

        name.length in 3..4 -> {
            "${name.take(1)}**${name.takeLast(1)}@$domain"
        }

        else -> {
            "${name.take(1)}*@$domain"
        }
    }
}

fun View.gone() { this.visibility = View.GONE }
fun View.visible() { this.visibility = View.VISIBLE }
fun View.invisible() { this.visibility = View.INVISIBLE }