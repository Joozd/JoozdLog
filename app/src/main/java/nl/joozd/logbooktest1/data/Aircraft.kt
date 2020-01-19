package nl.joozd.logbooktest1.data

import android.os.Parcel
import android.os.Parcelable

data class Aircraft(
    val id: Int,
    val registration: String,
    val manufacturer: String,
    val model: String,
    val engine_type: String,
    val mtow: Int,
    val se: Int,
    val me: Int,
    val multipilot: Int,
    val isIfr: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(registration)
        parcel.writeString(manufacturer)
        parcel.writeString(model)
        parcel.writeString(engine_type)
        parcel.writeInt(mtow)
        parcel.writeInt(se)
        parcel.writeInt(me)
        parcel.writeInt(multipilot)
        parcel.writeInt(isIfr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Aircraft> {
        override fun createFromParcel(parcel: Parcel): Aircraft {
            return Aircraft(parcel)
        }

        override fun newArray(size: Int): Array<Aircraft?> {
            return arrayOfNulls(size)
        }
    }
    val typeString = "$manufacturer $model"
}