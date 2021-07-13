package com.kutuzov.mapsearch.utils

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView


fun Context.showKeyboard(view : SearchView) {
    val inputMethodManager: InputMethodManager =
        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}
