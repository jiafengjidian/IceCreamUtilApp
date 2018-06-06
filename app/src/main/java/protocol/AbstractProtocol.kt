package protocol

import kotlin.experimental.and
import kotlin.experimental.or

/**
 * 通信协议
 * Header: 0xE1
 * End: 0xEF
 */

sealed class AbstractProtocol(val type: Int, args: ByteArray)
{
    val rawByteArray: ByteArray

    init
    {
        val len = 5 + args.size
        rawByteArray = ByteArray(len)
        rawByteArray[0] = 0xE1.toByte()
        rawByteArray[1] = rawByteArray.size.toByte()
        rawByteArray[2] = type.toByte()

        args.forEachIndexed { index, byte ->
            rawByteArray[3 + index] = byte
        }

        rawByteArray[len - 1] = 0xEF.toByte()
    }

    fun getByteArray(): ByteArray
    {
        var c = 0
        val len = rawByteArray.size
        for (i in 3 until (len - 2)) {
            c = c xor rawByteArray[i].toInt()
        }

        rawByteArray[len - 2] = c.toByte()
        return rawByteArray
    }

    protected inline fun arg(index: Int) = index + 2
}

class TestRobotArmProtocol(args: ByteArray) : AbstractProtocol(0x03, args)
{
    class Build
    {
        var robotArm1Position: Int = 0
        var robotArm2Position: Int = 0
        var robotArm1Speed: Int = 0
        var robotArm2Speed: Int = 0

        fun build(): TestRobotArmProtocol
        {
            val bytes = byteArrayOf((robotArm1Position shr 7).toByte(),
                                    (robotArm1Position and 0x7F).toByte(),
                                    (robotArm2Position shr 7).toByte(),
                                    (robotArm2Position and 0x7F).toByte(),
                                    robotArm1Speed.toByte(), robotArm2Speed.toByte())
            return TestRobotArmProtocol(bytes)
        }
    }
}

class TestMotoProtocol(args: ByteArray) : AbstractProtocol(0x04, args)
{
    class Build
    {
        private var arg1 = 0
        private var sp1 = 0
        private var sp2 = 0

        fun setFridgeAction(action: Int)
        {
            arg1 = arg1 and 0x0F
            arg1 = arg1 or (action shl 4)
        }

        fun setPickMotoAction(action: Int)
        {
            arg1 = arg1 and 0xF0
            arg1 = arg1 or action
        }

        fun setFridgeSpeed(sp: Int)
        {
            sp1 = sp
        }

        fun setPickMotoSpeed(sp: Int)
        {
            sp2 = sp
        }

        fun build() = TestMotoProtocol(byteArrayOf(arg1.toByte(), sp1.toByte(), sp2.toByte()))
    }
}

class TestShipmentProtocol(args: ByteArray) : AbstractProtocol(0x05, args)
{
    class Build
    {
        var x = 518
        var y = 518

        fun build() = TestShipmentProtocol(byteArrayOf( (x shr 7).toByte(), (x and 0x7F).toByte(),
                                                        (y shr 7).toByte(), (y and 0x7F).toByte()))
    }
}

class SettingGoodsTypeProtocol(args: ByteArray) : AbstractProtocol(0x06, args)
{
    class Buile
    {
        var row = 0
        var col = 0

        fun build() = SettingGoodsTypeProtocol(byteArrayOf( row.toByte(), col.toByte() ))
    }
}





