package service

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import util.log

class BluetoothService : Service(), Bluetooth
{
    private lateinit var bleManager: BluetoothManager
    private lateinit var bleAdapter: BluetoothAdapter
    private lateinit var bleGatt: BluetoothGatt
    private lateinit var bleChara: BluetoothGattCharacteristic
    private var bleConnectFlag = false

    override fun onCreate()
    {
        super.onCreate()
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
        return false
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

    }

    private val bleGattCallback = object: BluetoothGattCallback()
    {
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic)
        {
            super.onCharacteristicChanged(gatt, characteristic)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int)
        {
            super.onServicesDiscovered(gatt, status)
            if (status != BluetoothGatt.GATT_SUCCESS)
            {
                return
            }
            bleGatt.services.forEach {
                log("ble服务:${it.uuid}")
                it.characteristics.forEach {
                    log("ble属性:${it.uuid}")
                    it.descriptors.forEach {
                        log("ble描述:${it.uuid}")
                    }
                }
            }
            bleConnectFlag = true
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int)
        {
            super.onConnectionStateChange(gatt, status, newState)
            if (status != BluetoothGatt.GATT_SUCCESS)
            {
                return
            }
            when (newState)
            {
                BluetoothProfile.STATE_CONNECTED ->
                {
                    log("蓝牙开始连接")
                }

                BluetoothProfile.STATE_DISCONNECTED ->
                {
                    log("蓝牙已经断开连接")
                }
            }
        }
    }

    inner class BluetoothBinder : Binder()
    {
        fun getService() = this@BluetoothService
    }
}