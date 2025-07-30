package com.example.blackstone.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
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
        val layoutId = when (viewType) {
            1 -> R.layout.item_ranking_top1
            2 -> R.layout.item_ranking_top2
            3 -> R.layout.item_ranking_top3
            else -> R.layout.item_ranking
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].rank) {
            1 -> 1
            2 -> 2
            3 -> 3
            else -> 0
        }
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

            val context = itemView.context

            if (item.isMyAffiliation) {
                ViewCompat.setBackgroundTintList(
                    itemView,
                    ContextCompat.getColorStateList(context, R.color.WinnerFit_midPurple)
                )
                tvRanking.setTextColor(ContextCompat.getColor(context, R.color.white))
                tvName.setTextColor(ContextCompat.getColor(context, R.color.white))
                tvScore.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                ViewCompat.setBackgroundTintList(
                    itemView,
                    ContextCompat.getColorStateList(context, android.R.color.white)
                )

                if (item.rank in 1..3) {
                    tvRanking.setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    tvRanking.setTextColor(ContextCompat.getColor(context, R.color.WinnerFit_lightPurple))
                }

                tvName.setTextColor(ContextCompat.getColor(context, R.color.WinnerFit_purple))
                tvScore.setTextColor(ContextCompat.getColor(context, R.color.WinnerFit_purple))
            }
        }
    }
}