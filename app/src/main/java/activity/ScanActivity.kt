package activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.*
import app.App
import com.hontech.icecreamutilapp.R
import data.BluetoothDeviceInfo
import data.BluetoothDeviceManager

class ScanActivity : AppCompatActivity()
{
    private val mScanButton: Button by lazy { findViewById<Button>(R.id.id_scan_button) }
    private val mRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.id_scan_recycler_view) }
    private val mAdapter: RecyclerViewAdapter by lazy { RecyclerViewAdapter(::onItemClick) }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        initUi()
    }

    private fun initUi()
    {
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = GridLayoutManager(this, 2)
        mRecyclerView.addItemDecoration(RecyclerViewItemDecoration())

        mScanButton.setOnClickListener {

            ScanPopupWindow().show(mRecyclerView) {
                BluetoothDeviceManager.scanStop()
            }

            BluetoothDeviceManager.scanStart {
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun onItemClick(position: Int)
    {
        val intent = Intent(this, BluetoothActivity::class.java)
        startActivity(intent)
    }

    private class RecyclerViewItemDecoration : RecyclerView.ItemDecoration()
    {
        companion object
        {
            const val hSize = 60
            const val vSize = 60
        }

        override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView?)
        {
            super.getItemOffsets(outRect, itemPosition, parent)
            val col = itemPosition % 2
            if (itemPosition < 2)
            {
                outRect.top = vSize
            }
            outRect.bottom = vSize
            outRect.left = hSize - col * hSize / 2
        }
    }

    private class RecyclerViewItem(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val mTextName = itemView.findViewById<TextView>(R.id.id_item_scan_text_view_name)
        private val mTextRssi = itemView.findViewById<TextView>(R.id.id_item_scan_text_view_rssi)
        private val mTextAddress = itemView.findViewById<TextView>(R.id.id_item_scan_text_view_address)
        private val mCardView = itemView.findViewById<CardView>(R.id.id_item_scan_card_view)

        fun set(info: BluetoothDeviceInfo)
        {
            mTextName.text = info.name
            mTextAddress.text = info.address
            mTextRssi.text = "${info.rssi}dB"
        }

        fun setItemClick(position: Int, click: (Int) -> Unit)
        {
            mCardView.setOnClickListener {
                click(position)
            }
        }
    }

    private class RecyclerViewAdapter(val click: (Int) -> Unit) : RecyclerView.Adapter<RecyclerViewItem>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewItem
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scan_recycler_view, parent, false)
            return RecyclerViewItem(view)
        }

        override fun onBindViewHolder(holder: RecyclerViewItem, position: Int)
        {
            val info = BluetoothDeviceManager.get(position)
            holder.set(info)
            holder.setItemClick(position, click)
        }

        override fun getItemCount() = BluetoothDeviceManager.getDeviceCount()
    }

    private class ScanPopupWindow : PopupWindow()
    {
        companion object
        {
            private val mView = LayoutInflater.from(App.context).inflate(R.layout.popup_scan_wait, null)
        }

        private val mImageView = mView.findViewById<ImageView>(R.id.id_popup_scan_image_view)
        private val mButtonStop = mView.findViewById<Button>(R.id.id_popup_scan_button_stop)
        private val mAnimator = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f)

        init
        {
            mAnimator.duration = 1000
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.repeatCount = ObjectAnimator.INFINITE
            mAnimator.repeatMode = ObjectAnimator.RESTART

            val group = mView.parent
            if (group != null)
            {
                (group as ViewGroup).removeAllViews()
            }

            width = 600
            height = 800
            contentView = mView
        }

        fun show(view: View, dismissCallback: () -> Unit)
        {
            setOnDismissListener(dismissCallback)
            mButtonStop.setOnClickListener {
                mAnimator.cancel()
                dismiss()
            }
            mAnimator.start()
            showAtLocation(view, Gravity.CENTER, 0, 0)
        }
    }
}
