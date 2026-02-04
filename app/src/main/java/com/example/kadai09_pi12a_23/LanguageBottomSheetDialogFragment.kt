package com.example.kadai09_pi12a_23

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

/**
 * 语言选择 BottomSheet，点击入口后弹出。
 */
class LanguageBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var onLanguageSelected: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()
        val currentLang = LocaleManager.getAppLanguage(ctx)

        fun selectLanguage(lang: String) {
            if (lang == currentLang) {
                dismiss()
                return
            }
            LocaleManager.setAppLanguage(ctx, lang)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(LocaleManager.languageToTag(lang)))
            Snackbar.make(view, R.string.profile_language_changed, Snackbar.LENGTH_SHORT).show()
            onLanguageSelected?.invoke()
            dismiss()
            requireActivity().recreate()
        }

        view.findViewById<View>(R.id.lang_option_zh).setOnClickListener { selectLanguage(LocaleManager.LANG_ZH) }
        view.findViewById<View>(R.id.lang_option_ja).setOnClickListener { selectLanguage(LocaleManager.LANG_JA) }
        view.findViewById<View>(R.id.lang_option_en).setOnClickListener { selectLanguage(LocaleManager.LANG_EN) }
    }
}
