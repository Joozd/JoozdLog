package nl.joozd.logbooktest1.extensions

import android.animation.ValueAnimator
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

fun Toolbar.showAnimated(){
    if (this.visibility == View.GONE) {
        this.visibility= View.VISIBLE
        val animator = ValueAnimator.ofInt(0, this.height)
        animator.addUpdateListener {
            this.translationY = 1.0f * it.animatedValue as Int - height
            this.alpha = (it.animatedValue as Int / height.toFloat())

        }
        animator.apply{
            duration=500
            start()
        }
    }

}

fun Toolbar.hideAnimated() {
    if (this.visibility == View.VISIBLE) {
        val animator = ValueAnimator.ofInt(0, this.height)
        animator.addUpdateListener {
            this.translationY = -1.0f * it.animatedValue as Int
            this.alpha = 1.0f - (it.animatedValue as Int / height.toFloat())
            if (it.animatedValue as Int == this.height) this.visibility = View.GONE
        }
        animator.apply{
            duration=500
            start()
        }
    }
}
