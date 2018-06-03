package service

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import service.Bluetooth.Companion.ACTION_BLUETOOTH_CONNECTED
import service.Bluetooth.Companion.ACTION_BLUETOOTH_DISCONNECTED
import service.Bluetooth.Companion.ACTION_BLUETOOTH_MESSAGE_READY
import task.Task
import util.log
import util.toHexString

import java.util.*

class BluetoothService : Service(), Bluetooth
{
    companion object
    {
        private const val SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb"
        private const val CHARA_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
        private const val DES_UUID = "00002902-0000-1000-8000-00805f9b34fb"
    }

    private lateinit var bleManager: BluetoothManager
    private lateinit var bleAdapter: BluetoothAdapter
    private var bleGatt: BluetoothGatt? = null
    private lateinit var bleChara: BluetoothGattCharacteristic
    private lateinit var bleBroadcast: LocalBroadcastManager
    private var bleConnectFlag = false

    override fun onCreate()
    {
        super.onCreate()
        bleBroadcast = LocalBroadcastManager.getInstance(this)
        bleManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bleAdapter = bleManager.adapter
        if (!bleAdapter.isEnabled)
        {
            bleAdapter.enable()
        }
    }

    override fun onBind(intent: Intent?): IBinder
    {
        return BluetoothBinder()
    }

    override fun write(byteArray: ByteArray): Boolean
    {
        Task.AsyncHandler.post {

            bleChara.value = byteArray
            bleGatt!!.writeCharacteristic(bleChara)
            Thread.sleep(5)
        }
        return bleConnectFlag
    }

    override fun isConnected(): Boolean
    {
        return bleConnectFlag
    }

    override fun connect(address: String)
    {
        val device = bleAdapter.getRemoteDevice(address)
        bleGatt = device.connectGatt(this, true, bleGattCallback)
    }

    override fun disconnect()
    {
        bleGatt?.close()
    }

    private val bleGattCallback = object: BluetoothGattCallback()
    {
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic)
        {
            val bytes = characteristic.value
            updateBroadcast(ACTION_BLUETOOTH_MESSAGE_READY, bytes)
            log(bytes.toHexString())
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int)
        {
            if (status != BluetoothGatt.GATT_SUCCESS)
            {
                return
            }

            val service = bleGatt!!.getService(UUID.fromString(SERVICE_UUID))
            bleChara = service.getCharacteristic(UUID.fromString(CHARA_UUID))
            val des = bleChara.getDescriptor(UUID.fromString(DES_UUID))

            des.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            bleGatt!!.writeDescriptor(des)
            bleGatt!!.setCharacteristicNotification(bleChara, true)
            bleConnectFlag = true
            updateBroadcast(ACTION_BLUETOOTH_CONNECTED)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int)
        {
            if (status != BluetoothGatt.GATT_SUCCESS)
            {
                updateBroadcast(ACTION_BLUETOOTH_DISCONNECTED)
                bleConnectFlag = true
                return
            }
            when (newState)
            {
                BluetoothProfile.STATE_CONNECTED ->
                {
                    bleGatt!!.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED ->
                {
                    bleConnectFlag = true
                    updateBroadcast(ACTION_BLUETOOTH_DISCONNECTED)
                }
            }
        }
    }

    private inline fun updateBroadcast(action: String)
    {
        val intent = Intent(action)
        bleBroadcast.sendBroadcast(intent)
    }

    private inline fun updateBroadcast(action: String, value: ByteArray)
    {
        val intent = Intent(action)
        intent.putExtra(action, value)
        bleBroadcast.sendBroadcast(intent)
    }

    inner class BluetoothBinder : Binder()
    {
        fun getService() = this@BluetoothService
    }
}