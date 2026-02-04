package com.example.kadai09_pi12a_23

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * 应用内语言切换：持久化并应用 zh / ja / en
 */
object LocaleManager {

    private const val PREFS_NAME = "locale_prefs"
    private const val KEY_APP_LANGUAGE = "app_language"

    const val LANG_ZH = "zh"
    const val LANG_JA = "ja"
    const val LANG_EN = "en"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** 当前保存的语言代码：zh / ja / en，未设置时默认 ja */
    fun getAppLanguage(context: Context): String =
        prefs(context).getString(KEY_APP_LANGUAGE, null) ?: LANG_JA

    /** 保存语言并应用（需在 Fragment 中再调用 setApplicationLocales + recreate） */
    fun setAppLanguage(context: Context, lang: String) {
        prefs(context).edit().putString(KEY_APP_LANGUAGE, lang).apply()
    }

    /** 将 zh/ja/en 转为 BCP 47 tag，用于 setApplicationLocales */
    fun languageToTag(lang: String): String = when (lang) {
        LANG_ZH -> "zh-Hans-CN"
        LANG_JA -> "ja"
        LANG_EN -> "en"
        else -> "ja"
    }

    /** 启动时根据保存的语言设置 App 语言 */
    fun applySavedLocale(context: Context) {
        val lang = getAppLanguage(context)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageToTag(lang)))
    }
}
