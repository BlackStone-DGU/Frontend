package com.example.blackstone.health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blackstone.R
import com.example.blackstone.data.Exercise
import com.example.blackstone.data.ExerciseRepository  // 저장소 import

class HealthFragment : Fragment() {

    private lateinit var exerciseList: List<Exercise>
    private lateinit var adapter: ExerciseAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_health, container, false)

        exerciseList = ExerciseRepository.getExercises()

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recyclerExercise)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExerciseAdapter(exerciseList, this)
        recyclerView.adapter = adapter

        // 운동 업데이트 결과 수신 리스너 등록
        parentFragmentManager.setFragmentResultListener("exerciseUpdated", viewLifecycleOwner) { _, bundle ->
            val exerciseId = bundle.getInt("exerciseId", -1)
            if (exerciseId != -1) {
                adapter.notifyItemChanged(exerciseId)
            }
        }

        return view
    }
}