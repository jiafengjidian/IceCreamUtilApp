package popup

import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.App
import com.hontech.icecreamutilapp.R

class RobotArmSettingWindow
{
    companion object
    {
        val instance: RobotArmSettingWindow by lazy { RobotArmSettingWindow() }
    }

    private val mView = LayoutInflater.from(App.context).inflate(R.layout.popup_debug_robot_arm_setting, null)
    private val mTitleText = mView.findViewById<TextView>(R.id.id_popup_robot_arm_setting_title_text_view)
    private val mSpeedText = mView.findViewById<TextView>(R.id.id_popup_robot_arm_setting_speed_text_view)
    private val mSpeedSeekBar = mView.findViewById<SeekBar>(R.id.id_popup_robot_arm_setting_speed_seek_bar)
    private val mPositionEditText = mView.findViewById<EditText>(R.id.id_popup_robot_arm_setting_position_edit_text)
    private val mClearImageView = mView.findViewById<ImageView>(R.id.id_popup_robot_arm_setting_position_clear_image_view)
    private val mBrakeButton = mView.findViewById<Button>(R.id.id_popup_robot_arm_setting_brake_button)
    private val mSettingButton = mView.findViewById<Button>(R.id.id_popup_robot_arm_setting_setting_button)
    private var onSettingClick = {}
    private var onBrakeClick = {}

    init
    {
        mClearImageView.setOnClickListener {
            mPositionEditText.setText("")
        }

        mPositionEditText.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable)
            {
                if (s.isEmpty()) {
                    mClearImageView.visibility = View.INVISIBLE
                } else {
                    mClearImageView.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
            }
        })

        mSpeedSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                mSpeedText.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?)
            {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?)
            {
            }
        })

        mSettingButton.setOnClickListener { onSettingClick() }
        mBrakeButton.setOnClickListener { onBrakeClick() }
    }

    fun show(parent: View, title: String, settingClick: () -> Unit, brakeClick: () -> Unit)
    {
        mTitleText.text = title
        onSettingClick = settingClick
        onBrakeClick = brakeClick
        val group = mView.parent
        if (group != null) {
            (group as ViewGroup).removeAllViews()
        }
        val popup = PopupWindow(mView, 800, 1000, true)
        popup.isOutsideTouchable = true
        popup.showAtLocation(parent, Gravity.CENTER, 0, 0)
    }


    private constructor()
}