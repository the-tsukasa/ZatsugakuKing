package com.example.kadai09_pi12a_23

import android.content.Context
import android.content.SharedPreferences

/**
 * 雑学王｜经验（EXP）与等级系统
 * - 等级总数：10 级，依据累计 EXP 升级
 * - 经验类型：累积制 EXP（不会清零）
 * - Lv.10「雑学王」为最终等级
 */
object RankManager {

    private const val PREFS_NAME = "rank_prefs"
    private const val KEY_TOTAL_EXP = "total_exp"
    private const val KEY_TOTAL_CORRECT = "total_correct"
    private const val KEY_CONSECUTIVE_CORRECT = "consecutive_correct"

    /** 各等级所需累计 EXP（达到该值即为该等级）Lv.1=0, Lv.2=50, ... Lv.10=10000 */
    private val EXP_THRESHOLDS = intArrayOf(0, 50, 200, 500, 1000, 1800, 3000, 4800, 7300, 10000)

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** 累计 EXP */
    fun getTotalExp(context: Context): Int =
        prefs(context).getInt(KEY_TOTAL_EXP, 0)

    /** 增加 EXP（累积，不清零） */
    fun addExp(context: Context, amount: Int) {
        if (amount <= 0) return
        prefs(context).edit()
            .putInt(KEY_TOTAL_EXP, getTotalExp(context) + amount)
            .apply()
    }

    /** 累計正解数（统计用） */
    fun getTotalCorrect(context: Context): Int =
        prefs(context).getInt(KEY_TOTAL_CORRECT, 0)

    /** 當前連続正解数（用于连对奖励与清零） */
    fun getConsecutiveCorrect(context: Context): Int =
        prefs(context).getInt(KEY_CONSECUTIVE_CORRECT, 0)

    /** 答对：累計正解+1、連続正解+1（EXP 由调用方按规则计算后 addExp） */
    fun addCorrect(context: Context) {
        prefs(context).edit()
            .putInt(KEY_TOTAL_CORRECT, getTotalCorrect(context) + 1)
            .putInt(KEY_CONSECUTIVE_CORRECT, getConsecutiveCorrect(context) + 1)
            .apply()
    }

    /** 答错/跳过：連続正解清零 */
    fun addIncorrect(context: Context) {
        prefs(context).edit()
            .putInt(KEY_CONSECUTIVE_CORRECT, 0)
            .apply()
    }

    /** 当前等级（1〜10），由累计 EXP 决定 */
    fun getCurrentLevel(context: Context): Int {
        val exp = getTotalExp(context)
        for (i in EXP_THRESHOLDS.indices.reversed()) {
            if (exp >= EXP_THRESHOLDS[i]) return i + 1
        }
        return 1
    }

    /** 当前等级所需的最低 EXP（当前等级起点） */
    fun getExpForCurrentLevel(context: Context): Int {
        val level = getCurrentLevel(context)
        return EXP_THRESHOLDS[level - 1]
    }

    /** 下一等级所需 EXP（用于经验条上限）；Lv.10 时为 10000 */
    fun getExpForNextLevel(context: Context): Int {
        val level = getCurrentLevel(context)
        return if (level >= 10) EXP_THRESHOLDS[9] else EXP_THRESHOLDS[level]
    }

    /** 是否已满级（Lv.10） */
    fun isMaxLevel(context: Context): Boolean = getCurrentLevel(context) >= 10

    enum class Rank(val level: Int, val titleResId: Int, val titleOnlyResId: Int, val descResId: Int) {
        LV1(1, R.string.rank_lv1, R.string.rank_only_lv1, R.string.rank_desc_lv1),
        LV2(2, R.string.rank_lv2, R.string.rank_only_lv2, R.string.rank_desc_lv2),
        LV3(3, R.string.rank_lv3, R.string.rank_only_lv3, R.string.rank_desc_lv3),
        LV4(4, R.string.rank_lv4, R.string.rank_only_lv4, R.string.rank_desc_lv4),
        LV5(5, R.string.rank_lv5, R.string.rank_only_lv5, R.string.rank_desc_lv5),
        LV6(6, R.string.rank_lv6, R.string.rank_only_lv6, R.string.rank_desc_lv6),
        LV7(7, R.string.rank_lv7, R.string.rank_only_lv7, R.string.rank_desc_lv7),
        LV8(8, R.string.rank_lv8, R.string.rank_only_lv8, R.string.rank_desc_lv8),
        LV9(9, R.string.rank_lv9, R.string.rank_only_lv9, R.string.rank_desc_lv9),
        LV10(10, R.string.rank_lv10, R.string.rank_only_lv10, R.string.rank_desc_lv10)
    }

    fun getCurrentRank(context: Context): Rank {
        val level = getCurrentLevel(context)
        return Rank.entries[level - 1]
    }

    fun getRankTitle(context: Context): String =
        context.getString(getCurrentRank(context).titleResId)

    fun getRankTitleOnly(context: Context): String =
        context.getString(getCurrentRank(context).titleOnlyResId)

    fun getRankDescription(context: Context): String =
        context.getString(getCurrentRank(context).descResId)

    /**
     * 估算「再答对多少题」可升级（仅考虑基础 +10 EXP/题，向上取整）
     * 满级返回 0。
     */
    fun questionsUntilNextLevel(context: Context): Int {
        if (isMaxLevel(context)) return 0
        val exp = getTotalExp(context)
        val next = getExpForNextLevel(context)
        val need = (next - exp).coerceAtLeast(0)
        if (need <= 0) return 0
        return (need + 9) / 10
    }
}
