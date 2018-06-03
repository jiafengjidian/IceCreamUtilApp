package util

import android.util.Log

inline fun log(msg: String)
{
    Log.d("调试", msg)
}

inline fun ByteArray.toHexString(): String
{
    val sb = StringBuilder()
    this.forEach {
        sb.append(String.format("%02x ", it))
    }
    return sb.toString()
}

inline fun ByteArray.arg(index: Int) = this[index + 2]

inline fun ByteArray.isCorrect(): Boolean
{
    if ((this[0] != 0xE1.toByte()) || (this[size - 1] != 0xEF.toByte()) ) {
        return false
    }

    if (this[1].toInt() != size) {
        return false
    }

    var c = 0
    for (i in 3 until (size - 2)) {
        c = c xor this[i].toInt()
    }

    if (c.toByte() != this[size - 2]) {
        return false
    }

    return true
}

inline fun ByteArray.getPosition1(): Int
{
    return (arg(1).toInt() shl 7) + arg(2)
}

inline fun ByteArray.getPosition2(): Int
{
    return (arg(3).toInt() shl 7) + arg(4)
}

inline fun ByteArray.getSwitchStatus(): Int
{
    return (arg(5).toInt() shl 7) + arg(6)
}

