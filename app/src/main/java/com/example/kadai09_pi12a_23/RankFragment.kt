package com.example.kadai09_pi12a_23

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RankFragment : Fragment() {

    private var hasScrolledToUser = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rank, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()
        val list = view.findViewById<RecyclerView>(R.id.rank_list)
        val adapter = RankAdapter(emptyList())
        list.layoutManager = LinearLayoutManager(ctx)
        list.adapter = adapter
        list.setHasFixedSize(true)
        refreshRankData(view)
    }

    override fun onResume() {
        super.onResume()
        view?.let { refreshRankData(it) }
    }

    private fun refreshRankData(view: View) {
        val ctx = requireContext()
        val items = RankRepository.getRankList(ctx)
        val list = view.findViewById<RecyclerView>(R.id.rank_list)
        (list.adapter as? RankAdapter)?.updateItems(items)

        val currentUser = items.find { it.isCurrentUser }
        if (currentUser != null) {
            view.findViewById<TextView>(R.id.rank_my_score_value)?.text =
                ctx.getString(R.string.rank_score_format, currentUser.score)
            view.findViewById<TextView>(R.id.rank_my_rank_value)?.text =
                ctx.getString(R.string.rank_position_format, currentUser.rank)
            view.findViewById<TextView>(R.id.rank_my_title_value)?.text = currentUser.rankTitle

            if (!hasScrolledToUser) {
                val index = items.indexOf(currentUser)
                if (index >= 0) {
                    hasScrolledToUser = true
                    list.post { list.smoothScrollToPosition(index) }
                }
            }
        }
    }
}
