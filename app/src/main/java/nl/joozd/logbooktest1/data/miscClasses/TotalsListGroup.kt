package nl.joozd.logbooktest1.data.miscClasses

import android.os.Parcel
import android.os.Parcelable

// The idea is that the ExpandableListAdapter for total times wil get a list of TotalsListGroups, each containing a header name, and a list of items.
// Each item consists (for now) of a name (eg. ME-Piston) and a value in minutes (eg. 523)

class TotalsListItem(val valueName: String, val totalTime: Long) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readLong()
    )


    constructor(valueName: String, totalTime: Int): this(valueName, totalTime.toLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(valueName)
        parcel.writeLong(totalTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TotalsListItem> {
        override fun createFromParcel(parcel: Parcel): TotalsListItem {
            return TotalsListItem(parcel)
        }

        override fun newArray(size: Int): Array<TotalsListItem?> {
            return arrayOfNulls(size)
        }
    }
}

class TotalsListGroup(val title: String, var items: List<TotalsListItem>) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createTypedArrayList(TotalsListItem) ?: emptyList()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TotalsListGroup> {
        override fun createFromParcel(parcel: Parcel): TotalsListGroup {
            return TotalsListGroup(parcel)
        }

        override fun newArray(size: Int): Array<TotalsListGroup?> {
            return arrayOfNulls(size)
        }
    }
}