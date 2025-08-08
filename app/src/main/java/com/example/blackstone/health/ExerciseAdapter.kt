package com.example.blackstone.health

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.blackstone.R
import com.example.blackstone.data.Exercise

class ExerciseAdapter(
    private val list: List<Exercise>,
    private val parentFragment: Fragment
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val cnt: TextView = view.findViewById(R.id.tvCnt)
        val declaration: TextView = view.findViewById(R.id.tvDeclaration)
        val image: ImageView = view.findViewById(R.id.imageExercise)
        val idle: ImageView = view.findViewById(R.id.ivIdle)
        val healthFeed: ConstraintLayout = view.findViewById(R.id.healthFeed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_health, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = list[position]

        // 이름 + 목표 수치 + 단위 (소수점 표시: km는 1.5km, 나머지는 정수)
        val goalDisplay = if (exercise.unit == "km") {
            String.format("%.1f", exercise.goal)
        } else {
            exercise.goal.toInt().toString()
        }
        holder.name.text = "${exercise.name} $goalDisplay${exercise.unit}"

        // 남은 수치 계산
        val remain = exercise.goal - exercise.current
        val cntText = when {
            exercise.current == 0f -> "아직 시작하지 않았어요!"
            remain <= 0f -> "목표 달성!"
            else -> {
                val remainDisplay = if (exercise.unit == "km") {
                    String.format("%.1f", remain)
                } else {
                    remain.toInt().toString()
                }
                "$remainDisplay${exercise.unit}만 더 하면 목표 달성!"
            }
        }
        holder.cnt.text = cntText

        // 설명과 이미지
        holder.declaration.text = exercise.description
        holder.image.setImageResource(exercise.imageResId)

        val isCompleted = exercise.current >= exercise.goal

        val arrowResId = when {
            isCompleted -> R.drawable.ic_clear
            exercise.name.contains("러닝") -> R.drawable.ic_play_run
            else -> R.drawable.ic_play
        }

        holder.idle.setImageResource(arrowResId)

        // 상세 이동
        holder.healthFeed.setOnClickListener {
            val detailFragment = HealthDetailFragment.newInstance(exercise)
            parentFragment.parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, detailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int = list.size
}