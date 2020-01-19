package nl.joozd.logbooktest1.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_total_times.*
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.miscClasses.TotalsListGroup
import nl.joozd.logbooktest1.ui.adapters.TotalTimesExpandableListAdapter

class TotalTimesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_total_times)
        setSupportActionBar(totalTimesToolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.totalTimes)

        val totalTimesData: List<TotalsListGroup> = (intent.getParcelableArrayExtra("totalTimes") ?: emptyArray()).map{it as TotalsListGroup}

        val adapter = TotalTimesExpandableListAdapter(this, totalTimesData as MutableList<TotalsListGroup>) // maybe fill async and load while expanding items
        expandible_listview.setAdapter(adapter)
        expandible_listview.expandGroup(0)
    }
}
