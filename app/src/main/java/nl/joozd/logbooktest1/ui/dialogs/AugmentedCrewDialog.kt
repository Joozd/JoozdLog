package nl.joozd.logbooktest1.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.dialog_augmented_crew.view.*
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.miscClasses.CrewValue
import nl.joozd.logbooktest1.extensions.onTextChanged

/********************************************************************************************
 * Sends a crewValue to onSaveListener.onSave(crewValue).
 * crewValue
 */

class AugmentedCrewDialog: Fragment(){
    private var thisView: View? = null
    var crewValue: CrewValue? = null
    set(value){
        value?.let { crewVal ->
            field = value
            thisView?.let { view ->
                if (view.crewSizeEditText.text.toString() != crewVal.crewSize.toString()) view.crewSizeEditText.setText(crewVal.crewSize.toString())
                if (view.didLandingCheckbox.isChecked != crewVal.didLanding) view.didLandingCheckbox.isChecked = crewVal.didLanding
                if (view.didTakeoffCheckbox.isChecked != crewVal.didTakeoff) view.didTakeoffCheckbox.isChecked = crewVal.didTakeoff
                if (view.timeForTakeoffLandingEditText.text.toString() != crewVal.takeoffLandingTimes.toString()) view.timeForTakeoffLandingEditText.setText(crewVal.takeoffLandingTimes.toString())
            }
        }
    }

    class OnSaveListener(private val f: (crewValue: CrewValue) -> Unit){
        fun onSave(crewValue: CrewValue){
            f(crewValue)
        }
    }

    var onSaveListener: OnSaveListener? = null

    fun setOnSaveListener(f: (crewValue: CrewValue) -> Unit){
        onSaveListener = OnSaveListener(f)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_augmented_crew, container, false)
        thisView=view

        view.crewDownButton.setOnClickListener {
            crewValue?.let {
                if (it.crewSize > 0) crewValue=it.copy(crewSize = it.crewSize -1)
            }
        }
        view.crewUpButton.setOnClickListener {
            crewValue?.let {
                if (it.crewSize < 9) crewValue=it.copy(crewSize = it.crewSize +1)
            }
        }
        view.didTakeoffCheckbox.setOnCheckedChangeListener { _, b ->
            crewValue?.let {
                crewValue = it.copy(didTakeoff = b)
            }
        }
        view.didLandingCheckbox.setOnCheckedChangeListener { _, b ->
            crewValue?.let {
                crewValue = it.copy(didLanding = b)
            }
        }

        view.timeForTakeoffLandingEditText.onTextChanged { time ->
            crewValue?.let {
                if (time.isNotEmpty())  crewValue = it.copy(takeoffLandingTimes = time.toInt())
            }
        }

        view.cancelCrewDialogButton.setOnClickListener { fragmentManager?.popBackStack() }
        view.augmentedCrewDialogBackground.setOnClickListener{ fragmentManager?.popBackStack() }
        view.saveCrewDialogButon.setOnClickListener {
            crewValue?.let {
                onSaveListener?.onSave(it)
            }
            fragmentManager?.popBackStack()
        }
        crewValue = crewValue
        return view
    }
}