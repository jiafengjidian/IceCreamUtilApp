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
import android.widget.ImageView
import android.widget.TextView
import com.hontech.icecreamutilapp.R
import popup.*


class DebugFragment : Fragment()
{

    private lateinit var mRecyclerView: RecyclerView
    private val mAdapter = RecyclerViewAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_debug, null)
        initUi(view)
        return view
    }

    private fun initUi(view: View)
    {
        mRecyclerView = view.findViewById(R.id.id_fragment_debug_recycler_view)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = GridLayoutManager(context, 2)
        mRecyclerView.addItemDecoration(RecyclerViewItemDecoration())
    }

    private class RecyclerViewItemDecoration : RecyclerView.ItemDecoration()
    {
        companion object
        {
            private const val hSize = 60
            private const val vSize = 60
        }

        override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView?)
        {
            super.getItemOffsets(outRect, itemPosition, parent)
            val col = itemPosition % 2
            outRect.bottom = vSize
            if (itemPosition < 2) {
                outRect.top = vSize
            }
            outRect.left = hSize - col * hSize / 2
        }
    }

    private inner class RecyclerViewItem(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val mCardView = itemView.findViewById<CardView>(R.id.id_item_fragment_debug_card_view)
        private val mText = itemView.findViewById<TextView>(R.id.id_item_fragment_debug_text_view)
        private val mImage = itemView.findViewById<ImageView>(R.id.id_item_fragment_debug_image_view)
        private val mTextData = itemView.findViewById<TextView>(R.id.id_item_fragment_debug_text_view_data)


    }

    private inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewItem>()
    {
        override fun getItemCount() = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewItem
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fragment_debug_recycler_view, parent, false)
            return RecyclerViewItem(view)
        }

        override fun onBindViewHolder(holder: RecyclerViewItem, position: Int)
        {
        }
    }
}