package com.example.kadai09_pi12a_23

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

data class ProfileUiState(
    val nickname: String = "",
    val avatarResId: Int = 0,
    val currentPresetIndex: Int = 0,
    val rankTitle: String = "",
    val totalExp: Int = 0,
    val totalCorrect: Int = 0,
    val consecutiveCorrect: Int = 0,
    val currentLevel: Int = 1,
    /** 当前等级起点 EXP（用于经验条） */
    val expForCurrentLevel: Int = 0,
    /** 下一等级所需 EXP（满级时为 10000） */
    val expForNextLevel: Int = 50,
    /** 再答多少题可升级，满级为 0 */
    val questionsUntilNextLevel: Int = 0,
    val isMaxLevel: Boolean = false
)

class ProfileViewModel(private val context: Context) : ViewModel() {

    private val _uiState = MutableLiveData(ProfileUiState())
    val uiState: LiveData<ProfileUiState> = _uiState

    fun loadProfile() {
        val ctx = context
        val nickname = ProfileManager.getNickname(ctx)
        var avatarResId = ProfileManager.getAvatarResId(ctx)
        // 确保 avatarResId 有效，避免 setImageResource 因无效 ID 崩溃（如 SharedPreferences 损坏或旧版本残留）
        val safeAvatarResId = when {
            avatarResId == 0 -> AvatarConfig.presets[0].resId
            AvatarConfig.presets.any { it.resId == avatarResId } -> avatarResId
            else -> {
                ProfileManager.setAvatarResId(ctx, 0)
                AvatarConfig.presets[0].resId
            }
        }
        val totalExp = RankManager.getTotalExp(ctx)
        val currentLevel = RankManager.getCurrentLevel(ctx)
        val expForCurrent = RankManager.getExpForCurrentLevel(ctx)
        val expForNext = RankManager.getExpForNextLevel(ctx)
        val isMax = RankManager.isMaxLevel(ctx)
        _uiState.value = ProfileUiState(
            nickname = nickname,
            avatarResId = safeAvatarResId,
            currentPresetIndex = AvatarConfig.resIdToIndex(safeAvatarResId),
            rankTitle = RankManager.getRankTitle(ctx),
            totalExp = totalExp,
            totalCorrect = RankManager.getTotalCorrect(ctx),
            consecutiveCorrect = RankManager.getConsecutiveCorrect(ctx),
            currentLevel = currentLevel,
            expForCurrentLevel = expForCurrent,
            expForNextLevel = expForNext,
            questionsUntilNextLevel = RankManager.questionsUntilNextLevel(ctx),
            isMaxLevel = isMax
        )
    }

    fun setNickname(name: String) {
        ProfileManager.setNickname(context, name)
        _uiState.value = (_uiState.value ?: ProfileUiState()).copy(nickname = name)
    }

    fun setAvatarResId(resId: Int) {
        ProfileManager.setAvatarResId(context, if (resId == AvatarConfig.presets[0].resId) 0 else resId)
        val index = AvatarConfig.resIdToIndex(resId)
        _uiState.value = (_uiState.value ?: ProfileUiState()).copy(avatarResId = resId, currentPresetIndex = index)
    }

    fun refreshFromRank() {
        val ctx = context
        val current = _uiState.value ?: ProfileUiState()
        val totalExp = RankManager.getTotalExp(ctx)
        val currentLevel = RankManager.getCurrentLevel(ctx)
        _uiState.value = current.copy(
            rankTitle = RankManager.getRankTitle(ctx),
            totalExp = totalExp,
            totalCorrect = RankManager.getTotalCorrect(ctx),
            consecutiveCorrect = RankManager.getConsecutiveCorrect(ctx),
            currentLevel = currentLevel,
            expForCurrentLevel = RankManager.getExpForCurrentLevel(ctx),
            expForNextLevel = RankManager.getExpForNextLevel(ctx),
            questionsUntilNextLevel = RankManager.questionsUntilNextLevel(ctx),
            isMaxLevel = RankManager.isMaxLevel(ctx)
        )
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass != ProfileViewModel::class.java) {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
            return ProfileViewModel(context.applicationContext) as T
        }
    }
}
