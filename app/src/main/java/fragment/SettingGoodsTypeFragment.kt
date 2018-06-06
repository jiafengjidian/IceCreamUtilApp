package fragment

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import app.App
import com.hontech.icecreamutilapp.R
import data.GoodType
import data.GoodTypeManager

class SettingGoodsTypeFragment : Fragment()
{
    private lateinit var mTextView: TextView
    private lateinit var mRecyclerView: RecyclerView
    private val mAdapter = RecyclerViewAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_setting_goods_type, null)
        initUi(view)
        return view
    }

    private fun initUi(view: View)
    {
        mTextView = view.findViewById(R.id.id_fragment_setting_goods_type_text_view)
        mRecyclerView = view.findViewById(R.id.id_fragment_setting_goods_type_recycler_view)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.addItemDecoration(RecyclerViewItemDecoration())
        mRecyclerView.layoutManager = GridLayoutManager(context, 2)
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
            val col = itemPosition % 3
            outRect.bottom = vSize
            outRect.left = hSize - col * hSize / 2
            if (itemPosition < 3) {
                outRect.top = vSize
            }
        }
    }

    private class RecyclerViewItem(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val mLayout = itemView.findViewById<LinearLayout>(R.id.id_item_fragment_goods_type_lay_out)
        private val mTextViewGoodsType = itemView.findViewById<TextView>(R.id.id_item_fragment_goods_type_text_view_goods_type)
        private val mTextViewPosition = itemView.findViewById<TextView>(R.id.id_item_fragment_goods_type_text_view_position)

        fun set(index: Int)
        {
            val info = GoodTypeManager.get(index)
            mTextViewGoodsType.text = "货道:${info.goodsType.row}-${info.goodsType.col}"
            mTextViewPosition.text = "位置:(${info.position.x},${info.position.y})"
            mLayout.setOnClickListener {

                Toast.makeText(App.context, "正在出货：${info.goodsType.row}-${info.goodsType.col}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewItem>()
    {
        override fun onBindViewHolder(holder: RecyclerViewItem, position: Int)
        {
            holder.set(position)
        }

        override fun getItemCount() = GoodTypeManager.getCounter()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewItem
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_good_type, parent, false)
            return RecyclerViewItem(view)
        }
    }
}