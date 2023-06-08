package com.example.fu.ui.extensions

fun CharSequence.toTitleCase(): CharSequence {
    val sb = StringBuilder()
    var isFirstSymbol = true
    this.forEach {
        if (it.isLetter() && isFirstSymbol) {
            sb.append(it.toTitleCase())
            isFirstSymbol = false
        } else {
            sb.append(it.toLowerCase())
        }
    }
    return sb.toString()
}