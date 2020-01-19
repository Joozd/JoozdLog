package nl.joozd.logbooktest1.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import nl.joozd.logbooktest1.R
import java.time.LocalDate
import java.util.*


class DatePickerFragment : androidx.fragment.app.DialogFragment(), DatePickerDialog.OnDateSetListener {
    class OnDatePicked (private val f: (date: LocalDate) -> Unit) {
        fun datePicked(date: LocalDate){
            f(date)
        }
    }

    var onDatePicked: OnDatePicked? = null


    var viewToUpdate: EditText? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(context!!, R.style.DatePicker, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        onDatePicked?.datePicked(LocalDate.of(year, month+1, day))
        // Do something with the date chosen by the user
    }
}