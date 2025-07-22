import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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

        // 더미 데이터 모음
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

        // 미션 피드 데이터 반영
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

        // 티어 피드 데이터 반영
        val progressPercent = 76

        val progressView = view.findViewById<View>(R.id.viewTierProgress)
        val tvTierStatus = view.findViewById<TextView>(R.id.tvTierStatus)

        progressView.post {
            val fullWidth = (progressView.parent as View).width
            progressView.layoutParams.width = (fullWidth * (progressPercent / 100f)).toInt()
            progressView.requestLayout()
        }
        tvTierStatus.text = "실버까지 남은 단계 ($progressPercent%)"

        // 칼로리 피드 데이터 반영
        val kcalBurned = 1320
        updateCalorieResultView(view, kcalBurned)
    }

    fun updateCalorieResultView(view: View, kcalBurned: Int) {
        val weightLossKg = kcalBurned / 7700f
        val roundedLoss = String.format("%.1f", weightLossKg)

        val tvCalorieBurnedText = view.findViewById<TextView>(R.id.tvCalorieBurnedText)
        val tvWeightLossText = view.findViewById<TextView>(R.id.tvWeightLossText)

        tvCalorieBurnedText.text = "오늘 하루 동안 ${kcalBurned}kcal 소모하여"

        val unit = "kg"
        val suffix = "를 감량했어요!"
        val fullText = "$roundedLoss$unit$suffix"
        val spannable = SpannableString(fullText)

        spannable.setSpan(
            AbsoluteSizeSpan(60, true),
            0,
            roundedLoss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            AbsoluteSizeSpan(30, true),
            roundedLoss.length,
            roundedLoss.length + unit.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvWeightLossText.text = spannable
    }
}