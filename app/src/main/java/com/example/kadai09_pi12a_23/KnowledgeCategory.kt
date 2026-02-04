package com.example.kadai09_pi12a_23

/**
 * 知识中心：按ジャンル分区，内容后续补充
 */
data class KnowledgeCategory(
    val titleResId: Int,
    val descriptionResId: Int,
    val genreKey: String,
    val badgeResId: Int? = null
)

object KnowledgeCategories {
    fun list(): List<KnowledgeCategory> = listOf(
        KnowledgeCategory(R.string.genre_all, R.string.knowledge_desc_all, Genre.ALL, R.string.knowledge_badge_recommended),
        KnowledgeCategory(R.string.genre_japan, R.string.knowledge_desc_japan, Genre.JAPAN),
        KnowledgeCategory(R.string.genre_world, R.string.knowledge_desc_world, Genre.WORLD),
        KnowledgeCategory(R.string.genre_it, R.string.knowledge_desc_it, Genre.IT, R.string.knowledge_badge_trending),
        KnowledgeCategory(R.string.genre_anime_game, R.string.knowledge_desc_anime_game, Genre.ANIME_GAME),
        KnowledgeCategory(R.string.genre_food, R.string.knowledge_desc_food, Genre.FOOD),
        KnowledgeCategory(R.string.genre_history, R.string.knowledge_desc_history, Genre.HISTORY)
    )
}
