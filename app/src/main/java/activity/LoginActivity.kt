package activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle

import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.App
import com.hontech.icecreamutilapp.R
import data.LoginManager
import data.User
import task.Task

class LoginActivity: AppCompatActivity()
{
    companion object
    {
        private const val PERMISSION_REQ_CODE = 2
    }

    private val mEditTextId: EditText by lazy { findViewById<EditText>(R.id.id_login_edit_text_id) }
    private val mEditTextPassword: EditText by lazy { findViewById<EditText>(R.id.id_login_edit_text_pass_word) }
    private val mCheckBoxRemember: CheckBox by lazy { findViewById<CheckBox>(R.id.id_login_check_box_remember_id_pass_word) }
    private val mCheckBoxAuto: CheckBox by lazy { findViewById<CheckBox>(R.id.id_login_check_box_auto_login) }
    private val mImageViewIdClear: ImageView by lazy { findViewById<ImageView>(R.id.id_login_image_view_id_clear) }
    private val mImageViewPasswordClear: ImageView by lazy { findViewById<ImageView>(R.id.id_login_image_view_pass_word_clear) }
    private val mButtonLogin: Button by lazy { findViewById<Button>(R.id.id_login_button_login) }

    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUi()
        initPermission()
    }

    private fun onLogin(user: User)
    {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)
    }

    private fun initPermission()
    {
        val res = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (res != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQ_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_CODE)
        {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                showToast("无法使用蓝牙")
                finish()
            }
        }
    }

    private val mIdWatcher = object: TextWatcher
    {
        override fun afterTextChanged(s: Editable)
        {
            if (s.isEmpty())
            {
                mImageViewIdClear.visibility = View.INVISIBLE
                return
            }
            mImageViewIdClear.visibility = View.VISIBLE
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        {
        }
    }

    private val mPasswordWatcher = object: TextWatcher
    {
        override fun afterTextChanged(s: Editable)
        {
            if (s.isEmpty())
            {
                mImageViewPasswordClear.visibility = View.INVISIBLE
                return
            }
            mImageViewPasswordClear.visibility = View.VISIBLE
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        {
        }
    }

    private fun initUi()
    {

        mEditTextPassword.addTextChangedListener(mPasswordWatcher)
        mEditTextId.addTextChangedListener(mIdWatcher)

        mImageViewIdClear.setOnClickListener {

            mEditTextId.setText("")
        }
        mImageViewPasswordClear.setOnClickListener {

            mEditTextPassword.setText("")
        }

        val user = LoginManager.getUser()

        if (user != null)
        {

            when (user.mode)
            {
                User.MODE_AUTO ->
                {
                    // 开始登录
                    mCheckBoxAuto.isChecked = true
                    mCheckBoxRemember.isChecked = true
                    mEditTextId.setText(user.id)
                    mEditTextPassword.setText(user.password)
                    Task.UiHandler.postDelayed({onLogin(user)}, 500)
                }

                User.MODE_REMEMBER ->
                {
                    //
                    mCheckBoxAuto.isChecked = false
                    mCheckBoxRemember.isChecked = true
                    mEditTextId.setText(user.id)
                    mEditTextPassword.setText(user.password)
                }
            }
        }

        mCheckBoxAuto.setOnCheckedChangeListener { _, isChecked->

            if (isChecked)
            {
                mCheckBoxRemember.isChecked = true
            }
        }

        mButtonLogin.setOnClickListener {
            // 开始登录
            val id = mEditTextId.text
            if (id.isEmpty())
            {
                showToast("请输入账号")
                return@setOnClickListener
            }

            val password = mEditTextPassword.text
            if (password.isEmpty())
            {
                showToast("请输入密码")
                return@setOnClickListener
            }

            val mode = if (mCheckBoxAuto.isChecked)
            {
                User.MODE_AUTO
            }
            else if (mCheckBoxRemember.isChecked)
            {
                User.MODE_REMEMBER
            }
            else
            {
                User.MODE_DEFAULT
            }

            val user = User(id.toString(), password.toString(), mode)
            LoginManager.save(user)
            // 开始登录
            onLogin(user)
        }

    }

    private fun showToast(msg: String)
    {
        if (mToast == null)
        {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        }
        else
        {
            mToast !!.setText(msg)
            mToast !!.duration = Toast.LENGTH_SHORT
        }
        mToast !!.show()
    }

    private class LoginPopupWindow : PopupWindow()
    {
        companion object
        {
            val mView = LayoutInflater.from(App.context).inflate(R.layout.popup_login, null)
        }

        init
        {
            val group = mView.parent
            if (group != null)
            {
                (group as ViewGroup).removeAllViews()
            }
            width = 300
            height = 300
            contentView = mView
            isOutsideTouchable = true
            isFocusable = true
        }

        fun show(view: View)
        {
            showAtLocation(view, Gravity.CENTER, 0, 0)
        }
    }

}
