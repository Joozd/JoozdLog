package nl.joozd.logbooktest1.extensions

import android.text.Editable

fun Editable?.toInt() = this.toString().toInt() // will probably throw an exception if not correct string.