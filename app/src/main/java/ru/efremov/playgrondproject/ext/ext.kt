package ru.efremov.playgrondproject.ext

import android.view.View
import ru.efremov.playgrondproject.common.SafeClickListener

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}