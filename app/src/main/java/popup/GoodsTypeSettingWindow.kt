package popup

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import app.App
import com.hontech.icecreamutilapp.R

class GoodsTypeSettingWindow
{
    companion object
    {
        val instance: GoodsTypeSettingWindow by lazy { GoodsTypeSettingWindow() }
        private const val WIDTH = 600
        private const val HEIGHT = 700
    }

    private val mView = LayoutInflater.from(App.context).inflate(R.layout.popup_add_goods_type, null)
    private val mEditTextRow = mView.findViewById<EditText>(R.id.id_popup_goods_setting_edit_text_row)
    private val mEditTextCol = mView.findViewById<EditText>(R.id.id_popup_goods_setting_edit_text_col)
    private val mImageVieWClearCol = mView.findViewById<ImageView>(R.id.id_popup_goods_setting_image_view_col)
    private val mImageViewClearRow = mView.findViewById<ImageView>(R.id.id_popup_goods_setting_image_view_row)
    private var mPopupWindow: PopupWindow? = null

    init
    {
        mImageVieWClearCol.setOnClickListener {
            mEditTextCol.setText("")
        }

        mImageViewClearRow.setOnClickListener {
            mEditTextRow.setText("")
        }

        mEditTextCol.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable)
            {
                if (s.isEmpty()) {
                    mImageVieWClearCol.visibility = View.INVISIBLE
                } else {
                    mImageVieWClearCol.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
            }
        })

        mEditTextRow.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable)
            {
                if (s.isEmpty()) {
                    mImageViewClearRow.visibility = View.INVISIBLE
                } else {
                    mImageViewClearRow.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
            }
        })

    }

    fun show(view: View)
    {
        val group = mView.parent
        if (group != null) {
            (group as ViewGroup).removeAllViews()
        }
        
    }

}