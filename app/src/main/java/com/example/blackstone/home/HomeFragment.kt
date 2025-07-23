import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.example.blackstone.data.MissionItem
import java.util.Calendar

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 미션 피드 데이터 반영
        val dummyMissions = listOf(
            MissionItem("러닝", 5, "KM", "러닝 5km", completed = 3),
            MissionItem("플랭크", 4, "SET", "플랭크 4세트", completed = 0),
            MissionItem("스쿼트", 6, "SET", "스쿼트 6세트", completed = 6),
            MissionItem("푸쉬업", 5, "SET", "푸쉬업 5세트", completed = 1)
        )
        updateMissionFeed(view, dummyMissions)

        // 티어 피드 데이터 반영
        val progressPercent = 76
        updateTierFeed(view, progressPercent)

        // 칼로리 피드 데이터 반영
        val kcalBurned = 1320
        updateCalorieFeed(view, kcalBurned)

        // 랭킹 피드 데이터 반영
        updateRankingFeed(
            view,
            universityName = "동국대학교",
            universityRank = 7,
            majorName = "컴퓨터공학과",
            majorRank = 16
        )

        // 위클리 피드 데이터 반영
        val recentScores = listOf(1, 0, 2, 4, 3, 4, 1)
        updateWeeklyFeed(view, recentScores)
    }

    private fun updateMissionFeed(view: View, missions: List<MissionItem>) {
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

        missions.forEachIndexed { index, mission ->
            if (index < tvProgressIds.size && index < ivIconIds.size) {
                val tv = view.findViewById<TextView>(tvProgressIds[index])
                val iv = view.findViewById<ImageView>(ivIconIds[index])

                val isCompleted = mission.completed >= mission.target

                tv.text = if (isCompleted) "완료!" else "${mission.completed}/${mission.target} ${mission.unit}"
                val alpha = if (isCompleted) 0.5f else 1.0f

                tv.alpha = alpha
                iv.alpha = alpha
            }
        }
    }

    private fun updateTierFeed(view: View, progressPercent: Int) {
        val progressView = view.findViewById<View>(R.id.viewTierProgress)
        val tvTierStatus = view.findViewById<TextView>(R.id.tvTierStatus)

        // 진행 바 넓이 설정
        progressView.post {
            val fullWidth = (progressView.parent as View).width
            progressView.layoutParams.width = (fullWidth * (progressPercent / 100f)).toInt()
            progressView.requestLayout()
        }

        // 상태 텍스트 설정
        tvTierStatus.text = "실버까지 남은 단계 ($progressPercent%)"
    }

    private fun updateCalorieFeed(view: View, kcalBurned: Int) {
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

    private fun updateRankingFeed(view: View, universityName: String, universityRank: Int, majorName: String, majorRank: Int) {
        val tvUniversityRankLabel = view.findViewById<TextView>(R.id.tvUniversityRankLabel)
        val tvUniversityRankValue = view.findViewById<TextView>(R.id.tvUniversityRankValue)
        val tvMajorRankLabel = view.findViewById<TextView>(R.id.tvMajorRankLabel)
        val tvMajorRankValue = view.findViewById<TextView>(R.id.tvMajorRankValue)

        // 텍스트 설정
        tvUniversityRankLabel.text = "현재 ${universityName}는"
        tvMajorRankLabel.text = "현재 ${majorName}는"

        val universitySpannable = SpannableString("${universityRank}위").apply {
            setSpan(AbsoluteSizeSpan(60, true), 0, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        tvUniversityRankValue.text = universitySpannable

        val majorSpannable = SpannableString("${majorRank}위").apply {
            setSpan(AbsoluteSizeSpan(60, true), 0, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        tvMajorRankValue.text = majorSpannable
    }

    private fun updateWeeklyFeed(view: View, scores: List<Int>) {
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK).let { (it + 5) % 7 }
        val recent7Days = (0..6).map { daysOfWeek[(todayIndex - 6 + it + 7) % 7] }

        val textIds = listOf(
            R.id.tvDay0, R.id.tvDay1, R.id.tvDay2, R.id.tvDay3,
            R.id.tvDay4, R.id.tvDay5, R.id.tvDay6
        )
        val barIds = listOf(
            R.id.barDay0, R.id.barDay1, R.id.barDay2, R.id.barDay3,
            R.id.barDay4, R.id.barDay5, R.id.barDay6
        )

        val dpHeightMap = mapOf(
            0 to 0,
            1 to 25,
            2 to 50,
            3 to 75,
            4 to 100
        )

        for (i in 0..6) {
            val tv = view.findViewById<TextView>(textIds[i])
            val bar = view.findViewById<View>(barIds[i])

            tv.text = recent7Days[i]
            tv.setTypeface(
                ResourcesCompat.getFont(view.context,
                    if (i == 6) R.font.poppins_bold else R.font.poppins_light), Typeface.NORMAL
            )

            val dp = dpHeightMap[scores.getOrNull(i)?.coerceIn(0, 4) ?: 0] ?: 0
            val px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                view.resources.displayMetrics
            ).toInt()

            bar.layoutParams.height = px
            bar.requestLayout()
        }

        val todayScore = scores.getOrNull(6)?.coerceIn(0, 4) ?: 0
        view.findViewById<TextView>(R.id.tvMissionLeft).text = "오늘 남은 미션 ${4 - todayScore}개"
    }
}