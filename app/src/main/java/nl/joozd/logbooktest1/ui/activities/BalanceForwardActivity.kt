package nl.joozd.logbooktest1.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ExpandableListView
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_balance_forward.*
import kotlinx.android.synthetic.main.activity_balance_forward.my_toolbar
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.BalanceForward
import nl.joozd.logbooktest1.data.db.BalanceForwardDb
import nl.joozd.logbooktest1.ui.adapters.BalanceForwardAdapter
import nl.joozd.logbooktest1.ui.dialogs.AddBalanceForwardDialog
import nl.joozd.logbooktest1.ui.utils.CustomSnackbar
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class BalanceForwardActivity : AppCompatActivity() {
    companion object{
        const val TAG = "BALANCE_FORWARD_ACTIVITY"
    }

    private val balanceForwardDb = BalanceForwardDb()
    private var allBalanceForwards: List<BalanceForward> = emptyList()
    private lateinit var adapter: BalanceForwardAdapter

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_balance_forward, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.addBalanceForwardMenu -> {
            addBalanceButton.hide()
            val addBalanceForwardDialog = AddBalanceForwardDialog()
            addBalanceForwardDialog.balanceForwardId = balanceForwardDb.highestId+1
            Log.d(TAG, "id = ${addBalanceForwardDialog.balanceForwardId}")
            Log.d(TAG, "highestId = ${balanceForwardDb.highestId}")
            addBalanceForwardDialog.setOnSave {
                balanceForwardDb.saveBalanceForward(it)
                allBalanceForwards += it
                adapter.balancesForward = allBalanceForwards
            }
            addBalanceForwardDialog.setOnClose {
                addBalanceButton.show()
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.balanceForwardLayoutBelowToolbar, addBalanceForwardDialog)
                .addToBackStack(null)
                .commit()
            true
        }
        else -> false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        TODO("get confirmation on delete and undo SnackBar")
//        TODO("add these to totals")
        //TODO also change Totals to activity instead of fragment

        super.onCreate(savedInstanceState)


        allBalanceForwards = balanceForwardDb.requestAllBalanceForwards()
        Log.d(TAG, "${allBalanceForwards.size} records found")


        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_balance_forward)
        setSupportActionBar(my_toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.balanceForward)

        val expandableList: ExpandableListView = expandible_listview
        adapter = BalanceForwardAdapter(this, allBalanceForwards, expandible_listview)
        adapter.let {a->
            a.setOnActionImageViewClicked { bf ->
                alert("Are you sure you want to delete?") {
                    yesButton {
                        balanceForwardDb.deleteBalanceForward(bf)
                        allBalanceForwards = balanceForwardDb.requestAllBalanceForwards()
                        a.balancesForward = allBalanceForwards
                        val snackBar = CustomSnackbar.make(balanceForwardLayout)
                        snackBar.setMessage("Deleted Balance Forward")
                        snackBar.setOnActionBarShown { addBalanceButton.hide() }
                        snackBar.setOnActionBarGone { addBalanceButton.show() }
                        snackBar.setOnAction {
                            balanceForwardDb.saveBalanceForward(bf)
                            allBalanceForwards = balanceForwardDb.requestAllBalanceForwards()
                            a.balancesForward = allBalanceForwards
                            snackBar.dismiss()
                        }
                        snackBar.duration =1000*10
                        snackBar.show()
                    }
                    noButton { }
                }.show()
            }

            a.setOnItemClicked {bf ->
                addBalanceButton.hide()
                val addBalanceForwardDialog = AddBalanceForwardDialog()
                addBalanceForwardDialog.balanceForward = bf
                addBalanceForwardDialog.balanceForwardId = balanceForwardDb.highestId
                addBalanceForwardDialog.setOnSave {
                    balanceForwardDb.saveBalanceForward(it)
                    allBalanceForwards = allBalanceForwards.filter{bf -> bf.id != it.id} + it
                    adapter?.balancesForward = allBalanceForwards
                }
                addBalanceForwardDialog.setOnClose {
                    addBalanceButton.show()
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.balanceForwardLayoutBelowToolbar, addBalanceForwardDialog)
                    .addToBackStack(null)
                    .commit()
            }
        }
        expandableList.setAdapter(adapter)
        adapter.notifyDataSetChanged()

        addBalanceButton.setOnClickListener {
            addBalanceButton.hide()
            val addBalanceForwardDialog = AddBalanceForwardDialog()
            addBalanceForwardDialog.balanceForwardId = balanceForwardDb.highestId+1
            addBalanceForwardDialog.setOnSave {
                balanceForwardDb.saveBalanceForward(it)
                allBalanceForwards += it
                adapter.balancesForward = allBalanceForwards
            }
            addBalanceForwardDialog.setOnClose {
                addBalanceButton.show()
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.balanceForwardLayoutBelowToolbar, addBalanceForwardDialog)
                .addToBackStack(null)
                .commit()
        }
    }
}


