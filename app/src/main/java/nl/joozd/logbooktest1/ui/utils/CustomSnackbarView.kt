package nl.joozd.logbooktest1.ui.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.ContentViewCallback
import nl.joozd.logbooktest1.R

class CustomSnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {

    init {
        View.inflate(context, R.layout.view_snackbar_custom, this)
    }
    class OnBarShown (private val f: () -> Unit){
        fun actionOnBarShown(){
            f()
        }
    }
    class OnBarGone (private val f: () -> Unit){
        fun actionOnBarGone(){
            f()
        }
    }

    var onBarShown: OnBarShown? = null
    var onBarGone: OnBarGone? = null


    override fun animateContentIn(delay: Int, duration: Int) {
        onBarShown?.actionOnBarShown()
    }

    override fun animateContentOut(delay: Int, duration: Int) {
        onBarGone?.actionOnBarGone()
        Log.d("iets anders", "nog iets")

    }

}