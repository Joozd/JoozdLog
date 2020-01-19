package nl.joozd.logbooktest1.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_edit_aircraft.*
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.Aircraft
import nl.joozd.logbooktest1.data.db.AircraftDb
import nl.joozd.logbooktest1.ui.adapters.EditAircraftAdapter
import nl.joozd.logbooktest1.ui.dialogs.EditAircraftDialog
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

// TODO: Make search function
// TODO: Make add function
// TODO make edit function


class EditAircraftActivity : AppCompatActivity() {
    companion object{
        const val TAG = "EditAircraftActivity"
    }
    val aircraftDb = AircraftDb()
    private lateinit var allAircraft: List<Aircraft>
    private lateinit var editAircraftAdapter: EditAircraftAdapter

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_aircraft, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.addAircraftMenu -> {
            // addAircraftButton.hide()
            toast("TODO: make aircraft edit fragment")
            showEditAircraftDialog(null)

            true
        }
        else -> false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allAircraft = (intent.getParcelableArrayExtra("allAircraft") ?: emptyArray()).map{it as Aircraft}
        val missingAircraft = (intent.getParcelableArrayExtra("missingAircraft") ?: emptyArray()).map{it as Aircraft}
        Log.d(TAG,"found ${allAircraft.size} and ${missingAircraft.size} aircraft")


        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_edit_aircraft)
        setSupportActionBar(editAircraftToolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.editAircraft)

        editAircraftAdapter = EditAircraftAdapter(allAircraft, missingAircraft){
            editAircraft(it)
        }
        aircraftRecyclerView.layoutManager = LinearLayoutManager(this)
        aircraftRecyclerView.adapter = editAircraftAdapter


    }
    private fun editAircraft(aircraft: Aircraft) {
        // TODO("open aircraft editing fragment")
        toast("selected ${aircraft.registration}")
        showEditAircraftDialog(aircraft)
    }

    private fun showEditAircraftDialog(aircraft: Aircraft?){
        val editAircraftDialog = EditAircraftDialog()
        editAircraftDialog.aircraft = aircraft ?: editAircraftDialog.aircraft.copy(id=aircraftDb.highestId+1)
        editAircraftDialog.allAircraft = (intent.getParcelableArrayExtra("allAircraft") ?: emptyArray()).map{it as Aircraft}
        editAircraftDialog.setOnSave { ac ->
            Log.d(TAG, "this will save aircraft: $ac")
            aircraftDb.saveAircraft(ac)
            allAircraft = aircraftDb.requestAllAircraft().sortedBy { it.registration }

            editAircraftAdapter.allAircraft = allAircraft
            editAircraftAdapter.notifyDataSetChanged()



        }
        supportFragmentManager.beginTransaction()
            .add(R.id.editAircraftBelowToolbar, editAircraftDialog)
            .addToBackStack(null)
            .commit()
    }


}
