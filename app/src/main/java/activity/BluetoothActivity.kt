package activity

import android.app.Service
import android.content.*
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import app.App

import com.hontech.icecreamutilapp.R
import com.wang.avi.AVLoadingIndicatorView
import data.BluetoothDeviceManager
import fragment.DebugFragment
import fragment.GoodTypeFragment
import fragment.SettingFragment
import service.Bluetooth

import service.BluetoothService
import util.log

class BluetoothActivity : AppCompatActivity()
{
    private val mViewPager: ViewPager by lazy { findViewById<ViewPager>(R.id.id_bluetooth_view_pager) }
    private val mNavigationView: BottomNavigationView by lazy { findViewById<BottomNavigationView>(R.id.id_bluetooth_bottom_navigation_view) }
    private lateinit var mBluetoothAddress: String
    private val mWaitConnectPopupWindow = LoadingPopupWindow()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        initUi()
        registerService()
    }

    override fun onDestroy()
    {
        unregisterService()
        super.onDestroy()
    }

    private fun initNavigationView()
    {
        val states = arrayOf( intArrayOf(-android.R.attr.state_checked),  intArrayOf(android.R.attr.state_checked))
        val colors = intArrayOf(resources.getColor(android.R.color.darker_gray), resources.getColor(android.R.color.holo_blue_bright))
        val colorStateList = ColorStateList(states, colors)
        mNavigationView.itemTextColor = colorStateList
        mNavigationView.itemIconTintList = colorStateList
    }

    private fun initUi()
    {
        initNavigationView()

        mViewPager.adapter = ViewPagerAdapter(supportFragmentManager)

        mNavigationView.setOnNavigationItemSelectedListener {

            when (it.itemId)
            {
                R.id.menu_bluetooth_debug_action ->
                {
                    mViewPager.currentItem = 0
                }

                R.id.menu_bluetooth_setting_action ->
                {
                    mViewPager.currentItem = 1
                }

                R.id.menu_bluetooth_goodstype_action ->
                {
                    mViewPager.currentItem = 2
                }
            }
            true
        }

        mViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int)
            {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
            {
            }

            override fun onPageSelected(position: Int)
            {
                mNavigationView.selectedItemId = when (position)
                {
                    0 -> R.id.menu_bluetooth_debug_action
                    1 -> R.id.menu_bluetooth_setting_action
                    else -> R.id.menu_bluetooth_goodstype_action
                }
            }
        })
    }

    private inline fun registerService()
    {
        val filter = IntentFilter()
        filter.addAction(Bluetooth.ACTION_BLUETOOTH_DISCONNECTED)
        filter.addAction(Bluetooth.ACTION_BLUETOOTH_CONNECTED)
        LocalBroadcastManager.getInstance(this).registerReceiver(mBluetoothReceiver, filter)

        val intent = Intent(this, BluetoothService::class.java)
        bindService(intent, mBluetoothServiceConnection, Service.BIND_AUTO_CREATE)

        mBluetoothAddress = getIntent().getStringExtra(ScanActivity.EXTRA_BLUETOOTH_ADDRESS)
    }

    private inline fun unregisterService()
    {
        mWaitConnectPopupWindow.dismiss()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBluetoothReceiver)
        BluetoothDeviceManager.bleControl!!.disconnect()
        BluetoothDeviceManager.bleControl = null
        unbindService(mBluetoothServiceConnection)
    }

    private inline fun connect()
    {
        BluetoothDeviceManager.bleControl!!.disconnect()
        BluetoothDeviceManager.bleControl!!.connect(mBluetoothAddress)
        mWaitConnectPopupWindow.show(mNavigationView)
    }

    private val mBluetoothReceiver = object: BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent)
        {
            val action = intent.action
            when (action)
            {
                Bluetooth.ACTION_BLUETOOTH_CONNECTED ->
                {
                    mWaitConnectPopupWindow.dismiss()
                    log("蓝牙连接成功")
                    Toast.makeText(this@BluetoothActivity, "蓝牙已经成功连接", Toast.LENGTH_SHORT).show()
                }

                Bluetooth.ACTION_BLUETOOTH_DISCONNECTED ->
                {
                    log("蓝牙连接断开")
                    Toast.makeText(this@BluetoothActivity, "蓝牙已经断开连接", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val mBluetoothServiceConnection = object: ServiceConnection
    {
        override fun onServiceDisconnected(name: ComponentName)
        {
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder)
        {
            val bind = (service as BluetoothService.BluetoothBinder)
            BluetoothDeviceManager.bleControl = bind.getService()
            connect()
        }
    }

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager)
    {
        private val mFragments = arrayOf(DebugFragment(), SettingFragment(), GoodTypeFragment())

        override fun getCount() = mFragments.size

        override fun getItem(position: Int) = mFragments[position]
    }

    private class LoadingPopupWindow : PopupWindow(LayoutInflater.from(App.context).inflate(R.layout.popup_connection_wait, null), 600, 800)
    {
        private val mLoadingView = contentView.findViewById<AVLoadingIndicatorView>(R.id.id_popup_loading_view)

        init
        {
            isOutsideTouchable = false
            isFocusable = false
        }

        fun show(view: View)
        {
            mLoadingView.show()
            showAtLocation(view, Gravity.CENTER, 0, 0)
        }
    }

}