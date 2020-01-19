package nl.joozd.logbooktest1.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.miscClasses.TotalsListGroup
import nl.joozd.logbooktest1.data.miscClasses.TotalsListItem


class TotalTimesExpandableListAdapter (private val context: Context, private val dataList: MutableList<TotalsListGroup>) : BaseExpandableListAdapter() {
    companion object{
        const val TAG = "TotalTimesExpandableListAdapter"
    }
    private class SequentialSorter(var itemsList: List<TotalsListItem>){
        companion object {
            const val VALUE = 1
            const val KEY = 0
            const val UNSORTED = -1
        }
        var counter = 0
        var sortedBy = UNSORTED
        fun sortList(){
            when (counter %4){
                0 -> {
                    itemsList = itemsList.sortedBy{it.valueName}
                    sortedBy = KEY
                }
                1 -> {
                    itemsList = itemsList.sortedBy { it.valueName }.asReversed()
                    sortedBy = KEY
                }
                2 -> {
                    itemsList = itemsList.sortedBy { it.totalTime }
                    sortedBy = VALUE
                }
                3 -> {
                    itemsList = itemsList.sortedBy{it.totalTime}.asReversed()
                    sortedBy = VALUE
                }

            }
            counter += 1
        }
    }
    private val itemsLists = mutableListOf<SequentialSorter>()
    init {
        dataList.forEach {
            itemsLists.add(SequentialSorter(it.items))
        }
    }



    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return itemsLists[listPosition].itemsList[expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val expandedListData = getChild(listPosition, expandedListPosition) as TotalsListItem
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.totals_list_element, null)
        }
        val listElementNameView = convertView!!.findViewById<TextView>(R.id.listElementName)
        val listElementValueView = convertView!!.findViewById<TextView>(R.id.listElementValue)

        listElementNameView.text = expandedListData.valueName
        listElementValueView.text = "${expandedListData.totalTime/60}:${(expandedListData.totalTime%60).toString().padStart(2,'0')}"
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[listPosition].items.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.dataList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.dataList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val listTitle = (getGroup(listPosition) as TotalsListGroup).title
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.totals_list_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listTitle)
        val actionImageView = convertView.findViewById<ImageView>(R.id.actionImageView)

        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        actionImageView.setImageResource(if (itemsLists[listPosition].sortedBy == SequentialSorter.KEY) android.R.drawable.ic_menu_sort_alphabetically else android.R.drawable.ic_menu_sort_by_size )
        actionImageView.setOnClickListener{
            itemsLists[listPosition].sortList()
            Log.d(TAG, if (itemsLists[listPosition].sortedBy == SequentialSorter.VALUE) "android.R.drawable.ic_menu_sort_alphabetically" else "android.R.drawable.ic_menu_sort_by_size")
            notifyDataSetChanged()
            (convertView.parent as ExpandableListView).expandGroup(listPosition)
        }
        actionImageView.visibility = if (isExpanded && listPosition != 0) View.VISIBLE else View.GONE // don't sort top thingy. Bit of a hack but works for this specific case.

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return false
    }
}
