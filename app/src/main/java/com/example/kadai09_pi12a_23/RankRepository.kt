package com.example.kadai09_pi12a_23

import android.content.Context

/**
 * 排行榜数据：预留多用户/后端接口，当前返回本地占位数据 + 10 个虚拟用户（Lv.1～Lv.10）
 */
data class RankItem(
    val rank: Int,
    val nickname: String,
    val rankTitle: String,
    val score: Int,
    val avatarResId: Int,
    val isCurrentUser: Boolean = false
)

object RankRepository {

    private val VIRTUAL_USER_NAME_IDS = intArrayOf(
        R.string.rank_virtual_user_1,
        R.string.rank_virtual_user_2,
        R.string.rank_virtual_user_3,
        R.string.rank_virtual_user_4,
        R.string.rank_virtual_user_5,
        R.string.rank_virtual_user_6,
        R.string.rank_virtual_user_7,
        R.string.rank_virtual_user_8,
        R.string.rank_virtual_user_9,
        R.string.rank_virtual_user_10
    )

    /** 虚拟用户按等级使用的头像：Lv.1-4 默认，Lv.5 avatar_reward_lv5，Lv.6-9 avatar_reward_lv6，Lv.10 王冠 */
    private fun avatarForRank(rank: RankManager.Rank): Int = when (rank) {
        RankManager.Rank.LV10 -> R.drawable.avatar_reward_king
        RankManager.Rank.LV9, RankManager.Rank.LV8, RankManager.Rank.LV7, RankManager.Rank.LV6 ->
            R.drawable.avatar_reward_lv6
        RankManager.Rank.LV5 -> R.drawable.avatar_reward_lv5
        else -> R.drawable.avatar_student_default
    }

    /**
     * 获取排名列表：当前用户 + 10 个虚拟用户，按分数降序后赋排名。
     */
    fun getRankList(context: Context): List<RankItem> {
        val virtualUsers = RankManager.Rank.entries.mapIndexed { index, rank ->
            val nickname = context.getString(VIRTUAL_USER_NAME_IDS[index])
            val score = listOf(0, 5, 15, 30, 50, 80, 120, 170, 230, 300)[index]
            val avatar = avatarForRank(rank)
            RankItem(0, nickname, context.getString(rank.titleOnlyResId), score, avatar, false)
        }
        val nickname = ProfileManager.getNickname(context)
        val rankTitle = RankManager.getRankTitleOnly(context)
        val score = RankManager.getTotalCorrect(context)
        val avatarResId = ProfileManager.getAvatarResId(context)
        val currentUserResId = if (avatarResId == 0) R.drawable.avatar_student_default else avatarResId

        val currentUser = RankItem(
            rank = 0,
            nickname = nickname,
            rankTitle = rankTitle,
            score = score,
            avatarResId = currentUserResId,
            isCurrentUser = true
        )

        val combined = (listOf(currentUser) + virtualUsers)
            .sortedByDescending { it.score }

        return combined.mapIndexed { index, item ->
            item.copy(rank = index + 1)
        }
    }
}
