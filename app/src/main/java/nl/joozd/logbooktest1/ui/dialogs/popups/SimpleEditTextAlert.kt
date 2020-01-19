package nl.joozd.logbooktest1.ui.dialogs.popups

import android.app.Activity
import android.app.AlertDialog
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import nl.joozd.logbooktest1.R

/**
 * Will make a view with an editTextbox called "editText"
 */
class SimpleEditTextAlert(activity: Activity): AlertDialog.Builder(activity){
    val inflater = activity.layoutInflater
    private val editTextView = inflater.inflate(R.layout.alert_edit_text, null)
    val editText = editTextView.textInputEditText

    fun setHint(hint: String){
        editTextView.textInputLayout.hint = hint
    }

    override fun show(): AlertDialog {
        setView(editTextView)
        return super.show()
    }

}