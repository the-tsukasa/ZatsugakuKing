package com.example.kadai09_pi12a_23

import android.graphics.Outline
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.example.kadai09_pi12a_23.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()
        viewModel = ViewModelProvider(this, ProfileViewModel.Factory(ctx))[ProfileViewModel::class.java]

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            applyUiState(state)
        }
        viewModel.loadProfile()

        fun refreshProfile() {
            viewModel.loadProfile()
        }

        binding.profileAvatar.setOnClickListener {
            val state = viewModel.uiState.value ?: return@setOnClickListener
            val level = state.currentLevel
            var next = (state.currentPresetIndex + 1) % AvatarConfig.presets.size
            while (level < AvatarConfig.presets[next].unlockLevel) {
                next = (next + 1) % AvatarConfig.presets.size
                if (next == state.currentPresetIndex) break
            }
            if (level >= AvatarConfig.presets[next].unlockLevel) {
                val resId = AvatarConfig.presets[next].resId
                viewModel.setAvatarResId(resId)
                Snackbar.make(binding.root, R.string.profile_avatar_updated, Snackbar.LENGTH_SHORT).show()
                refreshProfile()
            }
        }

        fun showAnchoredTooltip(anchor: View, message: String) {
            val content = LayoutInflater.from(ctx).inflate(R.layout.tooltip_anchor, null)
            (content.findViewById<TextView>(R.id.tooltip_text)).text = message
            content.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val popup = PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popup.isOutsideTouchable = true
            popup.isFocusable = true
            popup.setBackgroundDrawable(null)
            popup.elevation = 12f
            val anchorWidth = anchor.width.coerceAtLeast(1)
            val xOff = ((anchorWidth - content.measuredWidth) / 2).coerceAtLeast(0)
            val yOff = (12 * resources.displayMetrics.density).toInt()
            popup.showAsDropDown(anchor, xOff, yOff, Gravity.START or Gravity.TOP)
            anchor.postDelayed({ if (popup.isShowing) popup.dismiss() }, 2000L)
        }

        val avatarOptViews = listOf(
            binding.profileAvatarOpt0,
            binding.profileAvatarOpt1,
            binding.profileAvatarOpt2,
            binding.profileAvatarOpt3,
            binding.profileAvatarOpt4
        )
        avatarOptViews.forEachIndexed { idx, img ->
            img.setOnClickListener {
                val state = viewModel.uiState.value ?: return@setOnClickListener
                val level = state.currentLevel
                val preset = AvatarConfig.presets[idx]
                if (level < preset.unlockLevel) {
                    showAnchoredTooltip(img, getString(R.string.profile_avatar_king_locked))
                    return@setOnClickListener
                }
                viewModel.setAvatarResId(preset.resId)
                Snackbar.make(binding.root, R.string.profile_avatar_updated, Snackbar.LENGTH_SHORT).show()
                refreshProfile()
            }
        }

        binding.profileAvatar.post {
            binding.profileAvatar.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setOval(0, 0, view.width, view.height)
                }
            }
            binding.profileAvatar.clipToOutline = true
        }

        binding.profileSave.setOnClickListener {
            val name = binding.profileNickname.text.toString().trim()
            val displayName = if (name.isEmpty()) getString(R.string.profile_default_nickname) else name
            viewModel.setNickname(displayName)
            Snackbar.make(binding.root, R.string.profile_nickname_saved_short, Snackbar.LENGTH_SHORT).show()
        }

        updateLanguageDisplay()
        binding.profileLanguageRow.setOnClickListener {
            LanguageBottomSheetDialogFragment().apply {
                onLanguageSelected = { updateLanguageDisplay() }
            }.show(childFragmentManager, "LanguageBottomSheet")
        }
    }

    private fun updateLanguageDisplay() {
        if (_binding == null) return
        val ctx = requireContext()
        val lang = LocaleManager.getAppLanguage(ctx)
        binding.profileLanguageValue.text = when (lang) {
            LocaleManager.LANG_ZH -> getString(R.string.language_zh)
            LocaleManager.LANG_JA -> getString(R.string.language_ja)
            LocaleManager.LANG_EN -> getString(R.string.language_en)
            else -> getString(R.string.language_ja)
        }
    }

    private fun applyUiState(state: ProfileUiState?) {
        if (state == null || _binding == null) return
        binding.profileNickname.setText(state.nickname)
        binding.profileRankBadge.text = state.rankTitle
        binding.profileStatExpValue.text = state.totalExp.toString()
        binding.profileStatTotalValue.text = state.totalCorrect.toString()
        binding.profileStatConsecutiveValue.text = state.consecutiveCorrect.toString()
        try {
            binding.profileAvatar.setImageResource(state.avatarResId)
        } catch (e: Exception) {
            binding.profileAvatar.setImageResource(AvatarConfig.presets[0].resId)
        }

        binding.profileExpBar.setProgress(
            state.totalExp - state.expForCurrentLevel,
            (state.expForNextLevel - state.expForCurrentLevel).coerceAtLeast(1)
        )
        binding.profileExpHint.text = if (state.isMaxLevel) {
            getString(R.string.profile_exp_level_max)
        } else {
            getString(R.string.exp_progress_hint, state.questionsUntilNextLevel, state.currentLevel + 1)
        }

        val avatarOptViews = listOf(
            binding.profileAvatarOpt0,
            binding.profileAvatarOpt1,
            binding.profileAvatarOpt2,
            binding.profileAvatarOpt3,
            binding.profileAvatarOpt4
        )
        avatarOptViews.forEachIndexed { idx, img ->
            val preset = AvatarConfig.presets[idx]
            val unlocked = state.currentLevel >= preset.unlockLevel
            img.alpha = if (unlocked) 1f else 0.45f
            img.setBackgroundResource(
                if (idx == state.currentPresetIndex) R.drawable.bg_avatar_circle_selected
                else R.drawable.bg_avatar_circle
            )
            val statusDesc = when {
                idx == state.currentPresetIndex -> getString(R.string.profile_avatar_opt_selected)
                !unlocked -> getString(R.string.profile_avatar_opt_unlock_level, preset.unlockLevel)
                else -> getString(R.string.profile_avatar_opt_available)
            }
            img.contentDescription = getString(R.string.profile_avatar_opt_desc, idx + 1, statusDesc)
        }

        binding.profileRankBadge.contentDescription = getString(R.string.profile_rank_badge_content_desc, state.rankTitle)
        binding.profileStatsCard.contentDescription = getString(
            R.string.profile_stats_content_desc,
            state.totalExp,
            state.totalCorrect,
            state.consecutiveCorrect
        )
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized) {
            viewModel.refreshFromRank()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
