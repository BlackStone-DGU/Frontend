import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.example.blackstone.data.MissionItem

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 데이터
        val dummyMissions = listOf(
            MissionItem("러닝", 5, "KM", "러닝 5km", completed = 3),
            MissionItem("플랭크", 4, "SET", "플랭크 4세트", completed = 2),
            MissionItem("스쿼트", 6, "SET", "스쿼트 6세트", completed = 6),
            MissionItem("푸쉬업", 5, "SET", "푸쉬업 5세트", completed = 1)
        )

        val tvProgressIds = listOf(
            R.id.tvMissionProgress1,
            R.id.tvMissionProgress2,
            R.id.tvMissionProgress3,
            R.id.tvMissionProgress4
        )

        val ivIconIds = listOf(
            R.id.ivMissionIcon1,
            R.id.ivMissionIcon2,
            R.id.ivMissionIcon3,
            R.id.ivMissionIcon4
        )

        // 오늘의 미션 데이터 반영
        dummyMissions.forEachIndexed { index, mission ->
            if (index < tvProgressIds.size) {
                val tv = view.findViewById<TextView>(tvProgressIds[index])
                val iv = view.findViewById<ImageView>(ivIconIds[index])

                val isCompleted = mission.completed >= mission.target

                tv.text = if (isCompleted) "완료!" else "${mission.completed}/${mission.target} ${mission.unit}"
                val alphaValue = if (isCompleted) 0.5f else 1.0f

                tv.alpha = alphaValue
                iv.alpha = alphaValue
            }
        }
    }
}