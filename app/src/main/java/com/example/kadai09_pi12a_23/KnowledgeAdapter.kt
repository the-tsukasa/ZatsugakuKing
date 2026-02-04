package com.example.kadai09_pi12a_23

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class KnowledgeAdapter(
    private var items: List<KnowledgeCategory>,
    private val onItemClick: (KnowledgeCategory) -> Unit
) : RecyclerView.Adapter<KnowledgeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_knowledge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.setText(item.titleResId)
        holder.description.setText(item.descriptionResId)
        if (item.badgeResId != null) {
            holder.badge.setText(item.badgeResId)
            holder.badge.isVisible = true
        } else {
            holder.badge.isVisible = false
        }
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.item_knowledge_title)
        val description: TextView = view.findViewById(R.id.item_knowledge_desc)
        val badge: TextView = view.findViewById(R.id.item_knowledge_badge)
    }
}
