package service

interface Bluetooth
{
    companion object
    {
        const val ACTION_BLUETOOTH_CONNECTED = "action.bluetooth.connected"
        const val ACTION_BLUETOOTH_DISCONNECTED = "action.bluetooth.disconnected"
        const val ACTION_BLUETOOTH_MESSAGE_READY = "action.bluetooth.message.ready"
        const val ACTION_BLUETOOTH_MESSAGE_WRITE = "action.bluetooth.message.write"
    }

    fun write(byteArray: ByteArray): Boolean

    fun isConnected(): Boolean

    fun connect(address: String)

    fun disconnect()

}
