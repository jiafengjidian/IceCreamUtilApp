package fragment

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.hontech.icecreamutilapp.R
import data.BluetoothDeviceManager
import data.GoodTypeManager

import protocol.TestShipmentProtocol
import util.log
import util.toHexString

class GoodTypeFragment : Fragment()
{
    private lateinit var mAddButton: Button
    private lateinit var mRecyclerView: RecyclerView
    private val mAdapter = RecyclerViewAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_good_type, null)
        initUi(view)
        return view
    }

    private fun initUi(view: View)
    {
        mAddButton = view.findViewById(R.id.id_fragment_good_type_add)
        mRecyclerView = view.findViewById(R.id.id_fragment_good_type_recycler_view)

        mRecyclerView.addItemDecoration(RecyclerViewItemDecoration())
        mRecyclerView.layoutManager = GridLayoutManager(context, 2)
        mRecyclerView.adapter = mAdapter

        mAddButton.setOnClickListener(::onClick)
    }

    private fun onClick(view: View)
    {
        GoodTypeManager.add()
        mAdapter.notifyDataSetChanged()
    }

    private class RecyclerViewItemDecoration : RecyclerView.ItemDecoration()
    {
        companion object
        {
            private const val hSize = (1080 - 450 * 2) / 3
            private const val vSize = hSize
        }

        override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView?)
        {
            super.getItemOffsets(outRect, itemPosition, parent)
            outRect.bottom = vSize
            val col = itemPosition % 2
            outRect.left = hSize - col * hSize / 2
            if (itemPosition < 2) {
                outRect.top = vSize
            }
        }
    }

    private class RecyclerViewItem(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val mCardView = itemView.findViewById<CardView>(R.id.id_item_good_type_card_view)
        private val mTextPosition = itemView.findViewById<TextView>(R.id.id_item_good_type_text_view_position)
        private val mTextName = itemView.findViewById<TextView>(R.id.id_item_good_type_text_view_name)

        fun set(index: Int)
        {
            val info = GoodTypeManager.get(index)
            mTextName.text = info.name
            mTextPosition.text = "(${info.position.x},${info.position.y})"
            mCardView.setOnClickListener {

                val bytes = TestShipmentProtocol.Build().apply {
                    x = info.position.x
                    y = info.position.y
                }.build().getByteArray()
                log(bytes.toHexString())
                BluetoothDeviceManager.bleControl!!.write(bytes)
            }
        }
    }

    private class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewItem>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewItem
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_good_type, parent, false)
            return RecyclerViewItem(view)
        }

        override fun getItemCount() = GoodTypeManager.getCounter()

        override fun onBindViewHolder(holder: RecyclerViewItem, position: Int)
        {
            holder.set(position)
        }
    }



}