package com.example.kadai09_pi12a_23

/**
 * ジャンル別クイズ用の定数
 * 値は quiz_questions.json の "genre" と一致させる
 */
object Genre {
    const val ALL = "すべて"
    const val JAPAN = "日本"
    const val WORLD = "世界"
    const val IT = "IT・テクノロジー"
    const val ANIME_GAME = "アニメ・ゲーム"
    const val FOOD = "食べ物"
    const val HISTORY = "歴史"

    val LIST = listOf(ALL, JAPAN, WORLD, IT, ANIME_GAME, FOOD, HISTORY)
}
