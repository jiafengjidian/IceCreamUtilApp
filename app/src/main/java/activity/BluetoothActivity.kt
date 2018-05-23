package activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

import com.hontech.icecreamutilapp.R
import fragment.DebugFragment

class BluetoothActivity : AppCompatActivity()
{
    private val mViewPager: ViewPager by lazy { findViewById<ViewPager>(R.id.id_bluetooth_view_pager) }
    private val mNavigationView: BottomNavigationView by lazy { findViewById<BottomNavigationView>(R.id.id_bluetooth_bottom_navigation_view) }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        initUi()
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

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager)
    {
        private val mFragments = arrayOf(DebugFragment(), Fragment(), Fragment())

        override fun getCount() = mFragments.size

        override fun getItem(position: Int) = mFragments[position]
    }

}