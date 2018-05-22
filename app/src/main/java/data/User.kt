package data

import android.os.Parcel
import android.os.Parcelable

/**
 * 保存用户名信息
 */
data class User(val id: String, val password: String, val mode: Int) : Parcelable
{
    companion object
    {
        const val MODE_DEFAULT = 0
        const val MODE_AUTO = 1
        const val MODE_REMEMBER = 2

        @JvmField
        val CREATOR = object: Parcelable.Creator<User>
        {
            override fun createFromParcel(source: Parcel): User
            {
                return User(source)
            }

            override fun newArray(size: Int): Array<User?>
            {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(src: Parcel) : this(src.readString(), src.readString(), src.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int)
    {
        dest.writeString(id)
        dest.writeString(password)
    }

    override fun describeContents(): Int
    {
        return 0
    }
}