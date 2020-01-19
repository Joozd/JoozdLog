package nl.joozd.logbooktest1.ui.utils

import android.view.View
import android.widget.AdapterView

class OnSpinnerItemSelectedListener: AdapterView.OnItemSelectedListener{
    class ItemSelected(private val f: (parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit){
        fun select (parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            f(parent, view, position, id)
        }
    }
    var onItemSelectedListener: ItemSelected? = null
    fun setOnItemSelectedListener (f: (parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit){
        onItemSelectedListener = ItemSelected(f)
    }

    class NothingSelected(private val f: (parent: AdapterView<*>?) -> Unit){
        fun select (parent: AdapterView<*>?) {
            f(parent)
        }
    }
    var onNothingSelectedListener: NothingSelected? = null
    fun setOnNothingSelectedListener (f: (parent: AdapterView<*>?) -> Unit){
        onNothingSelectedListener = NothingSelected(f)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemSelectedListener?.select(parent, view, position, id)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        onNothingSelectedListener?.select(parent)
    }

}