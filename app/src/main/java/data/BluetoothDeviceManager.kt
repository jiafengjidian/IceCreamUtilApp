package data

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import app.App
import service.Bluetooth

object BluetoothDeviceManager
{
    var bleControl: Bluetooth? = null

    private val infoList = ArrayList<BluetoothDeviceInfo>()
    private val bleAdapter = (App.context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private val bleScanner = bleAdapter.bluetoothLeScanner
    private var scanResultCallback = {}

    fun getDeviceCount() = infoList.size

    fun get(index: Int) = infoList[index]

    fun scanStart(callback: () -> Unit)
    {
        if (!bleAdapter.isEnabled)
        {
            bleAdapter.enable()
        }
        scanResultCallback = callback
        bleScanner.startScan(scanCallback)
    }

    fun scanStop()
    {
        bleScanner.stopScan(scanCallback)
    }

    private val scanCallback = object: ScanCallback()
    {
        override fun onScanResult(callbackType: Int, result: ScanResult)
        {
            val device = result.device
            val rssi = result.rssi
            val name = if (device.name == null) {
                "未知设备"
            } else {
                device.name
            }
            val info = BluetoothDeviceInfo(name, device.address, rssi)
            if (! infoList.contains(info))
            {
                infoList.add(info)
                scanResultCallback()
            }
        }
    }

}