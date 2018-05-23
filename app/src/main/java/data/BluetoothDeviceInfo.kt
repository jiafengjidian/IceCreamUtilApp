package data

data class BluetoothDeviceInfo(val name: String, val address: String, val rssi: Int)
{
    override fun equals(other: Any?): Boolean
    {
        return address.equals((other as BluetoothDeviceInfo).address)
    }

}