package com.example.blackstone.health

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blackstone.R

class ExerciseAdapter(private val list: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.textExerciseName)
        val time: TextView = view.findViewById(R.id.textExerciseTime)
        val image: ImageView = view.findViewById(R.id.imageExercise)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_health, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = list[position]
        holder.name.text = exercise.name
        holder.time.text = exercise.time
        holder.image.setImageResource(exercise.imageResId)
    }

    override fun getItemCount(): Int = list.size
}
