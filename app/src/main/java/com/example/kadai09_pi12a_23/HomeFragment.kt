package com.example.kadai09_pi12a_23

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.fragment.app.Fragment
import com.example.kadai09_pi12a_23.ExpBarView

class HomeFragment : Fragment() {

    private lateinit var rankTitleText: TextView
    private lateinit var homeExpBarView: ExpBarView
    private lateinit var homeExpText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rankTitleText = view.findViewById(R.id.rankTitleText)
        homeExpBarView = view.findViewById(R.id.home_exp_bar_view)
        homeExpText = view.findViewById(R.id.home_exp_text)

        updateRankDisplay()
        updateExpBar()

        fun startQuiz(genre: String) {
            startActivity(Intent(requireContext(), QuizActivity::class.java).putExtra(QuizActivity.EXTRA_GENRE, genre))
        }

        view.findViewById<MaterialButton>(R.id.homeStartButton).setOnClickListener { startQuiz(Genre.ALL) }
        view.findViewById<Button>(R.id.genreBlockAll).setOnClickListener { startQuiz(Genre.ALL) }
        view.findViewById<Button>(R.id.genreBlockJapan).setOnClickListener { startQuiz(Genre.JAPAN) }
        view.findViewById<Button>(R.id.genreBlockWorld).setOnClickListener { startQuiz(Genre.WORLD) }
        view.findViewById<Button>(R.id.genreBlockIt).setOnClickListener { startQuiz(Genre.IT) }
        view.findViewById<Button>(R.id.genreBlockAnimeGame).setOnClickListener { startQuiz(Genre.ANIME_GAME) }
        view.findViewById<Button>(R.id.genreBlockFood).setOnClickListener { startQuiz(Genre.FOOD) }
        view.findViewById<Button>(R.id.genreBlockHistory).setOnClickListener { startQuiz(Genre.HISTORY) }
    }

    override fun onResume() {
        super.onResume()
        if (::rankTitleText.isInitialized) updateRankDisplay()
        if (::homeExpBarView.isInitialized) updateExpBar()
    }

    private fun updateRankDisplay() {
        if (!::rankTitleText.isInitialized) return
        rankTitleText.text = getString(R.string.rank_current) + " " + RankManager.getRankTitle(requireContext())
    }

    private fun updateExpBar() {
        if (!::homeExpBarView.isInitialized || !::homeExpText.isInitialized) return
        val ctx = requireContext()
        val totalExp = RankManager.getTotalExp(ctx)
        val expCurrentLevel = RankManager.getExpForCurrentLevel(ctx)
        val expNextLevel = RankManager.getExpForNextLevel(ctx)
        homeExpText.text = getString(R.string.exp_bar_format, totalExp, expNextLevel)
        val max = 100
        val targetProgress: Int
        if (RankManager.isMaxLevel(ctx)) {
            targetProgress = 100
        } else {
            val range = (expNextLevel - expCurrentLevel).coerceAtLeast(1)
            targetProgress = ((totalExp - expCurrentLevel).coerceIn(0, range) * 100 / range).toInt()
        }
        homeExpBarView.setProgressAnimated(homeExpBarView.getProgress(), targetProgress, max)
    }
}
