package view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.hontech.icecreamutilapp.R

class CustomSeekBar : LinearLayout
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val mTextView: TextView
    private val mSeekBar: SeekBar

    init
    {
        LayoutInflater.from(context).inflate(R.layout.custom_seek_bar, this, true)
        mTextView = findViewById(R.id.id_custom_text_view)
        mSeekBar = findViewById(R.id.id_custom_seek_bar)
        mSeekBar.max = 100
        mSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                mTextView.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?)
            {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?)
            {
            }
        })
    }

    fun seekBar() = mSeekBar
}