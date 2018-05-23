package popup

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.App
import com.hontech.icecreamutilapp.R

sealed class DebugPopupWindow(contentView: View, val title: String) : PopupWindow(contentView, 600, 800)
{
    init
    {
        isFocusable = true
    }

    open fun show(view: View)
    {
        val group = contentView.parent
        if (group != null)
        {
            (group as ViewGroup).removeAllViews()
        }
    }
}

class RobotSettingPopupWindow(title: String, private val okClick: (RobotSettingPopupWindow) -> Unit)
    : DebugPopupWindow(LayoutInflater.from(App.context).inflate(R.layout.popup_debug_moto_setting, null), title)
{
    private val mButtonCancel = contentView.findViewById<Button>(R.id.id_popup_debug_moto_setting_button_cancel)
    private val mButtonOk = contentView.findViewById<Button>(R.id.id_popup_debug_moto_setting_button_ok)
    private val mEditTextValue = contentView.findViewById<EditText>(R.id.id_popup_debug_moto_setting_edit_text_value)
    private val mTextViewCurrentValue = contentView.findViewById<TextView>(R.id.id_popup_debug_moto_setting_text_view_current_value)
    private val mTextViewTitle = contentView.findViewById<TextView>(R.id.id_popup_debug_moto_setting_text_view_title)
    private val mImageClear = contentView.findViewById<ImageView>(R.id.id_popup_debug_moto_setting_image_view_clear)

    init
    {
        mTextViewCurrentValue.text = "0"
        mTextViewTitle.text = title

        mButtonCancel.setOnClickListener { dismiss() }
        mButtonOk.setOnClickListener {
            dismiss()
            okClick(this@RobotSettingPopupWindow)
        }

        mEditTextValue.addTextChangedListener(object: TextWatcher
        {
            override fun afterTextChanged(s: Editable)
            {
                if (s.isEmpty()) {
                    mImageClear.visibility = View.INVISIBLE
                } else {
                    mImageClear.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
            }
        })

        mImageClear.setOnClickListener { mEditTextValue.setText("") }
    }

    fun getStepText(): String
    {
        return mEditTextValue.text.toString()
    }

    override fun show(view: View)
    {
        super.show(view)
        showAtLocation(view, Gravity.CENTER, 0, 0)
    }
}


