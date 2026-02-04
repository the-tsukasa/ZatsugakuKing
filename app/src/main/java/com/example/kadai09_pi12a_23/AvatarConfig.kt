package com.example.kadai09_pi12a_23

/**
 * 头像预设配置：资源 ID 与解锁等级。
 * 0 表示默认头像（avatar_student_default）；新增头像只需在此扩展。
 */
data class AvatarPreset(
    val resId: Int,
    val unlockLevel: Int
)

object AvatarConfig {
    /** 预设列表：默认、动物1、Lv5、Lv6、动物2、王冠 */
    val presets: List<AvatarPreset> = listOf(
        AvatarPreset(R.drawable.avatar_student_default, 0),
        AvatarPreset(R.drawable.avatar_student_animal_1, 0),
        AvatarPreset(R.drawable.avatar_reward_lv5, 5),
        AvatarPreset(R.drawable.avatar_reward_lv6, 6),
        AvatarPreset(R.drawable.avatar_student_animal_2, 10),
        AvatarPreset(R.drawable.avatar_reward_king, 10)
    )

    fun resIdToIndex(resId: Int): Int {
        if (resId == 0) return 0
        return presets.indexOfFirst { it.resId == resId }.takeIf { it >= 0 } ?: 0
    }
}
