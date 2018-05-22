package data

import android.content.Context
import app.App

object LoginManager
{
    private const val EXTRA_LOGIN_ID_KEY = "extra.login.id.key"
    private const val EXTRA_LOGIN_PASS_WORD_KEY = "extra.login.pass.word.key"
    private const val EXTRA_LOGIN_MODE_KEY = "extra.login.mode.key"
    private const val SHARE_PRE_NAME = "app.share.pre.name"

    fun save(user: User)
    {
        val share = App.context.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE)
        share.edit()
                .putString(EXTRA_LOGIN_ID_KEY, user.id)
                .putString(EXTRA_LOGIN_PASS_WORD_KEY, user.password)
                .putInt(EXTRA_LOGIN_MODE_KEY, user.mode)
                .commit()
    }

    fun getUser(): User?
    {
        val share = App.context.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE)
        val id = share.getString(EXTRA_LOGIN_ID_KEY, "")
        if (id.isEmpty()) {
            return null
        }
        val password = share.getString(EXTRA_LOGIN_PASS_WORD_KEY, "")
        if (password.isEmpty()) {
            return null
        }
        val mode = share.getInt(EXTRA_LOGIN_MODE_KEY, 0)
        return User(id, password, mode)
    }



}