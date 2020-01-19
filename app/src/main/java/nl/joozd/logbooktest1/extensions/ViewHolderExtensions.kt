package nl.joozd.logbooktest1.extensions

import android.view.ViewGroup
import android.widget.TextView


fun ViewGroup.colorAllChildren(color: Int){
    for (c in 0 until childCount){
        val v = getChildAt(c)
        if (v is TextView) v.setTextColor(color)
    }
}