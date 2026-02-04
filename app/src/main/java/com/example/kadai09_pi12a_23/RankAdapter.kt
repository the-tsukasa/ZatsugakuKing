package com.example.kadai09_pi12a_23

import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RankAdapter(
    private var items: List<RankItem>
) : RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    fun updateItems(newItems: List<RankItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rank, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val ctx = holder.itemView.context

        holder.rankNumber.text = item.rank.toString()
        holder.nickname.text = item.nickname
        holder.rankTitle.text = item.rankTitle
        holder.scoreText.text = ctx.getString(R.string.rank_score_format, item.score)
        holder.avatar.setImageResource(
            if (item.avatarResId != 0) item.avatarResId else R.drawable.avatar_student_default
        )
        holder.avatar.post {
            holder.avatar.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setOval(0, 0, view.width, view.height)
                }
            }
            holder.avatar.clipToOutline = true
        }

        // 当前用户：高亮背景 + 「你」标签
        holder.itemRankRoot.setBackgroundResource(
            if (item.isCurrentUser) R.drawable.bg_rank_item_me else R.drawable.bg_rank_item_normal
        )
        holder.youBadge.visibility = if (item.isCurrentUser) View.VISIBLE else View.GONE

        // 前三名：奖牌圆 + 奖牌文字色
        when (item.rank) {
            1 -> {
                holder.medalBg.visibility = View.VISIBLE
                holder.medalBg.setBackgroundResource(R.drawable.bg_rank_medal_gold)
                holder.rankNumber.setTextColor(ContextCompat.getColor(ctx, R.color.rank_medal_gold_text))
            }
            2 -> {
                holder.medalBg.visibility = View.VISIBLE
                holder.medalBg.setBackgroundResource(R.drawable.bg_rank_medal_silver)
                holder.rankNumber.setTextColor(ContextCompat.getColor(ctx, R.color.rank_medal_silver_text))
            }
            3 -> {
                holder.medalBg.visibility = View.VISIBLE
                holder.medalBg.setBackgroundResource(R.drawable.bg_rank_medal_bronze)
                holder.rankNumber.setTextColor(ContextCompat.getColor(ctx, R.color.rank_medal_bronze_text))
            }
            else -> {
                holder.medalBg.visibility = View.GONE
                holder.rankNumber.setTextColor(ContextCompat.getColor(ctx, R.color.manga_ink_secondary))
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemRankRoot: LinearLayout = view.findViewById(R.id.item_rank_root)
        val medalBg: View = view.findViewById(R.id.item_rank_medal_bg)
        val rankNumber: TextView = view.findViewById(R.id.item_rank_number)
        val avatar: ImageView = view.findViewById(R.id.item_rank_avatar)
        val nickname: TextView = view.findViewById(R.id.item_rank_nickname)
        val rankTitle: TextView = view.findViewById(R.id.item_rank_title)
        val scoreText: TextView = view.findViewById(R.id.item_rank_score)
        val youBadge: TextView = view.findViewById(R.id.item_rank_you_badge)
    }
}
