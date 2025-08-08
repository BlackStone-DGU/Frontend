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

class HealthFragment : Fragment() {

    private lateinit var exerciseList: MutableList<Exercise>
    private lateinit var adapter: ExerciseAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_health, container, false)

        // 운동 목록을 mutableList로 생성
        exerciseList = mutableListOf(
            Exercise(
                name = "스쿼트", unit = "개", goal = 20f, current = 0f,
                description = "스쿼트는 하체 근력과 코어 안정성을 강화하는 대표적인 전신 운동입니다.",
                imageResId = R.drawable.ic_squat_mission
            ),
            Exercise(
                name = "러닝", unit = "km", goal = 3f, current = 1.2f,
                description = "러닝은 심폐 기능을 향상시키고 체지방 감량에 효과적인 유산소 운동입니다.",
                imageResId = R.drawable.ic_run_mission
            ),
            Exercise(
                name = "푸시업", unit = "개", goal = 10f, current = 6f,
                description = "푸시업은 상체 근력과 코어를 함께 단련할 수 있는 대표적인 맨몸 운동입니다.",
                imageResId = R.drawable.ic_pushup_mission
            ),
            Exercise(
                name = "플랭크", unit = "초", goal = 60f, current = 60f,
                description = "플랭크는 코어 안정성과 자세 교정에 효과적인 정적 운동입니다.",
                imageResId = R.drawable.ic_plank_mission
            )
        )

        // RecyclerView 연결
        recyclerView = view.findViewById(R.id.recyclerExercise)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExerciseAdapter(exerciseList, this)
        recyclerView.adapter = adapter

        // 스쿼트 운동 결과 수신 리스너 등록
        parentFragmentManager.setFragmentResultListener("squatUpdated", viewLifecycleOwner) { _, bundle ->
            val updatedCurrent = bundle.getFloat("updatedCurrent", 0f)

            // 스쿼트 항목 찾아서 current 값 갱신
            val index = exerciseList.indexOfFirst { it.name.contains("스쿼트") }
            if (index != -1) {
                exerciseList[index].current = updatedCurrent
                adapter.notifyItemChanged(index)
            }
        }

        return view
    }
}