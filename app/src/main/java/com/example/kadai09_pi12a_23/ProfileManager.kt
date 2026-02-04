package com.example.kadai09_pi12a_23

import android.content.Context
import android.content.SharedPreferences

/**
 * 个人中心：昵称、头像（预设）本地存储（无登录）
 */
object ProfileManager {

    private const val PREFS_NAME = "profile_prefs"
    private const val KEY_NICKNAME = "nickname"
    private const val KEY_AVATAR_RES_ID = "avatar_res_id"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getNickname(context: Context): String =
        prefs(context).getString(KEY_NICKNAME, null) ?: context.getString(R.string.profile_default_nickname)

    fun setNickname(context: Context, nickname: String) {
        prefs(context).edit().putString(KEY_NICKNAME, nickname).apply()
    }

    /** 预设头像资源 ID，0 表示默认（avatar_default） */
    fun getAvatarResId(context: Context): Int =
        prefs(context).getInt(KEY_AVATAR_RES_ID, 0)

    fun setAvatarResId(context: Context, resId: Int) {
        prefs(context).edit().putInt(KEY_AVATAR_RES_ID, resId).apply()
    }
}
