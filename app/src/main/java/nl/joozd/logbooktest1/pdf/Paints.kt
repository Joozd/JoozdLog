package nl.joozd.logbooktest1.pdf


import android.graphics.Paint
import android.graphics.Typeface

object Paints {
    val smallTextCentered = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF000000.toInt()
        textAlign = Paint.Align.CENTER
        textSize=9f
    }
    val smallTextRight = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF000000.toInt()
        textAlign = Paint.Align.RIGHT
        textSize=9f
    }

    val smallText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF000000.toInt()
        textAlign = Paint.Align.LEFT
        textSize=9f
    }

    val largeText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF000000.toInt()
        textAlign = Paint.Align.CENTER
        textSize = 11f
        val x: Typeface? = null
        typeface = Typeface.create(x, Typeface.BOLD)
    }
    val largeTextRightAligned = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF000000.toInt()
        textAlign = Paint.Align.RIGHT
        textSize = 11f
        val x: Typeface? = null
        typeface = Typeface.create(x, Typeface.BOLD)
    }


    val mediumText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF000000.toInt()
        textAlign = Paint.Align.CENTER
        textSize=10f
    }


    val thickLine = Paint().apply {
        color = 0xFF000000.toInt()
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }
    val thinLine = Paint().apply {
        color = 0xFF000000.toInt()
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }
}