package nl.joozd.logbooktest1.extensions

import android.graphics.Typeface
import android.widget.TextView
import nl.joozd.logbooktest1.R

fun TextView.showAsActive(){
    this.alpha=1.0F
    this.setTypeface(null, Typeface.BOLD)
    this.setBackgroundResource(R.drawable.rounded_corners_white)
}

fun TextView.showAsInactive(){
    this.alpha=0.5F
    this.setTypeface(null, Typeface.NORMAL)
    this.setBackgroundResource(0)
}