package fragment


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.hontech.icecreamutilapp.R
import data.BluetoothDeviceManager
import data.DeviceStatusManager
import protocol.TestMotoProtocol
import service.Bluetooth
import util.RESULT_TYPE_STATUS
import view.CustomEditText
import view.CustomSeekBar

class SettingFragment : Fragment()
{
    private lateinit var mPositionTextView: TextView
    private lateinit var mRobot1Custom: CustomEditText
    private lateinit var mRobot2Custom: CustomEditText
    private lateinit var mPickMotoCustom: CustomSeekBar
    private lateinit var mFridgeCustom: CustomSeekBar
    private lateinit var mPickMotoUpButton: Button
    private lateinit var mPickMotoDownButton: Button
    private lateinit var mFridgeUpButton: Button
    private lateinit var mFridgeDownButton: Button
    private lateinit var mLocalBroadcast: LocalBroadcastManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_setting, null)
        initUi(view)
        return view
    }

    private fun initUi(view: View)
    {
        mRobot1Custom = view.findViewById(R.id.id_fragment_setting_robot1_custom_edit_text)
        mRobot2Custom = view.findViewById(R.id.id_fragment_setting_robot2_custom_edit_text)
        mPickMotoCustom = view.findViewById(R.id.id_fragment_setting_pick_moto_seek_bar)
        mFridgeCustom = view.findViewById(R.id.id_fragment_setting_fridge_seek_bar)
        mPickMotoDownButton = view.findViewById(R.id.id_fragment_setting_pick_moto_down)
        mPickMotoUpButton = view.findViewById(R.id.id_fragment_setting_pick_moto_up)
        mFridgeDownButton = view.findViewById(R.id.id_fragment_setting_fridge_down)
        mFridgeUpButton = view.findViewById(R.id.id_fragment_setting_fridge_up)
        mPositionTextView = view.findViewById(R.id.id_fragment_setting_text_view)
        mRobot1Custom.edit().hint = "位置1"
        mRobot2Custom.edit().hint = "位置2"

        mPickMotoDownButton.setOnClickListener(::onPickMotoDown)
        mPickMotoUpButton.setOnClickListener(::onPickMotoUp)
        mFridgeDownButton.setOnClickListener(::onFridgeDown)
        mFridgeUpButton.setOnClickListener(::onFridgeUp)

        mLocalBroadcast = LocalBroadcastManager.getInstance(context!!)
        val filter = IntentFilter(DeviceStatusManager.ACTION_POSITION_CHANGED)
        mLocalBroadcast.registerReceiver(mReceiver, filter)
    }

    private val mReceiver = object: BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent)
        {
            val action = intent.action
            when (action)
            {
                DeviceStatusManager.ACTION_POSITION_CHANGED ->
                {
                    val p = DeviceStatusManager.getPosition()
                    mPositionTextView.text = "(${p.x},${p.y})"
                }
            }
        }
    }

    private fun onPickMotoUp(view: View)
    {
        val sp = mPickMotoCustom.seekBar().progress
        val protocol = TestMotoProtocol.Build().apply {
            setPickMotoAction(1)
            setPickMotoSpeed(sp)
        }.build()
        BluetoothDeviceManager.bleControl!!.write(protocol.getByteArray())
    }

    private fun onPickMotoDown(view: View)
    {
        val sp = mPickMotoCustom.seekBar().progress
        val protocol = TestMotoProtocol.Build().apply {
            setPickMotoAction(2)
            setPickMotoSpeed(sp)
        }.build()
        BluetoothDeviceManager.bleControl!!.write(protocol.getByteArray())
    }

    private fun onFridgeUp(view: View)
    {
        val sp = mFridgeCustom.seekBar().progress
        val bytes = TestMotoProtocol.Build().apply {
            setFridgeAction(1)
            setFridgeSpeed(sp)
        }.build().getByteArray()
        BluetoothDeviceManager.bleControl!!.write(bytes)
    }

    private fun onFridgeDown(view: View)
    {
        val sp = mFridgeCustom.seekBar().progress
        val bytes = TestMotoProtocol.Build().apply {
            setFridgeSpeed(2)
            setFridgeSpeed(sp)
        }.build().getByteArray()
        BluetoothDeviceManager.bleControl!!.write(bytes)
    }

    override fun onDestroyView()
    {
        mLocalBroadcast.unregisterReceiver(mReceiver)
        super.onDestroyView()
    }
}