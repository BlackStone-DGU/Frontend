package com.example.blackstone.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blackstone.R
import java.text.NumberFormat
import java.util.Locale

class RankingAdapter : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    private var items: List<RankingItem> = emptyList()

    fun setItems(newItems: List<RankingItem>) {
        items = newItems
        notifyDataSetChanged() // 전체 갱신
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRanking: TextView = itemView.findViewById(R.id.tvRanking)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvScore: TextView = itemView.findViewById(R.id.tvScore)

        fun bind(item: RankingItem) {
            tvRanking.text = "${item.rank}위"
            tvName.text = item.name
            tvScore.text = "${NumberFormat.getNumberInstance(Locale.US).format(item.score)}점"

        }
    }
}