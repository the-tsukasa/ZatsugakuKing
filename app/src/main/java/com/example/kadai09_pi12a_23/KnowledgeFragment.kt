package com.example.kadai09_pi12a_23

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class KnowledgeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_knowledge, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = view.findViewById<RecyclerView>(R.id.knowledge_list)
        val adapter = KnowledgeAdapter(KnowledgeCategories.list()) { _ ->
            Toast.makeText(requireContext(), R.string.knowledge_coming_soon, Toast.LENGTH_SHORT).show()
        }
        list.layoutManager = LinearLayoutManager(requireContext())
        list.isNestedScrollingEnabled = false
        list.adapter = adapter
    }
}
