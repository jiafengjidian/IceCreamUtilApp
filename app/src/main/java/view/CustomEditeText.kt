package view

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.media.Image
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.hontech.icecreamutilapp.R

class CustomEditText : LinearLayout
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val mEditText: EditText
    private val mImageView: ImageView

    init
    {
        setBackgroundResource(R.drawable.shape_rounded)
        LayoutInflater.from(context).inflate(R.layout.custom_edite_text, this, true)
        mEditText = findViewById(R.id.id_custom_edit_text)
        mImageView = findViewById(R.id.id_custom_image_view)

        mEditText.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable)
            {
                if (s.isEmpty()) {
                    mImageView.visibility = View.INVISIBLE
                } else {
                    mImageView.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
            }
        })
        mImageView.setOnClickListener {
            mEditText.setText("")
        }

        mEditText.gravity = Gravity.CENTER
    }

    fun edit() = mEditText
}
