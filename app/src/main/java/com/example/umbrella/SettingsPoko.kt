package com.example.umbrella

import android.os.Parcel
import android.os.Parcelable

data class SettingsPoko(
    val zip : String,
    val unit : Int
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(zip)
        parcel.writeInt(unit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SettingsPoko> {
        const val FAHRENHEIT: Int = 0
        const val CELCIUS : Int = 1
        override fun createFromParcel(parcel: Parcel): SettingsPoko {
            return SettingsPoko(parcel)
        }

        override fun newArray(size: Int): Array<SettingsPoko?> {
            return arrayOfNulls(size)
        }
    }
}