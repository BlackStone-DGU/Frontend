import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blackstone.health.Exercise
import com.example.blackstone.health.ExerciseAdapter
import com.example.blackstone.R

public class HealthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_health, container, false)

        // 1. 데이터 생성
        val exerciseList = listOf(
            Exercise("스쿼트 20개", "약 3분 소요", R.drawable.ic_run_mission),
            Exercise("푸쉬업 15개", "약 2분 소요", R.drawable.ic_run_mission),
            Exercise("런지 20개", "약 4분 소요", R.drawable.ic_run_mission)
        )

        // 2. RecyclerView 연결
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerExercise)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ExerciseAdapter(exerciseList)

        return view
    }
}
