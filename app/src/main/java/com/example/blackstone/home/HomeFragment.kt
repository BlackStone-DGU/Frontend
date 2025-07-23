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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.example.blackstone.data.MissionItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 헤더 피드 데이터 반영
        updateHeaderFeed(view, 120)

        // 미션 피드 데이터 반영
        val dummyMissions = listOf(
            MissionItem("러닝", 5, "KM", "러닝 5km", completed = 3),
            MissionItem("플랭크", 4, "SET", "플랭크 4세트", completed = 0),
            MissionItem("스쿼트", 6, "SET", "스쿼트 6세트", completed = 6),
            MissionItem("푸쉬업", 5, "SET", "푸쉬업 5세트", completed = 1)
        )
        updateMissionFeed(view, dummyMissions)

        // 피드 랜덤 구성 및 나머지 피드 데이터 반영
        addRandomFeeds(view, 1532)

        // 특정 피드 클릭 시 프래그먼트 전환
        handleFeedClicks(view)
    }

    private fun updateHeaderFeed(view: View, dDayCount: Int) {
        val tvDday = view.findViewById<TextView>(R.id.tvDday)
        val tvHeaderTitle = view.findViewById<TextView>(R.id.tvHeaderTitle)

        if (dDayCount <= 0) {
            tvDday.text = "D-0"
            tvHeaderTitle.text = "오늘부터 달려볼까요?"
        } else {
            tvDday.text = "D+$dDayCount"
            tvHeaderTitle.text = "우리가 달려온 시간"
        }
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

    private fun updateContributionFeed(view: View, rank: Int, groupName: String, totalScore: Int, myName: String, myScore: Int) {
        val tvRank = view.findViewById<TextView>(R.id.tvUniversityRank)
        val tvGroupName = view.findViewById<TextView>(R.id.tvUniversityName)
        val tvTotalScore = view.findViewById<TextView>(R.id.tvUniversityTotalScore)
        val tvUserId = view.findViewById<TextView>(R.id.tvUserId)
        val tvMyScore = view.findViewById<TextView>(R.id.tvMyScore)

        // 쉼표 단위 포맷
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        val formattedTotalScore = numberFormat.format(totalScore)
        val formattedMyScore = numberFormat.format(myScore)

        // 등수 텍스트 및 폰트 크기 조절
        tvRank.text = "${rank}위"
        tvRank.textSize = when {
            rank < 10 -> 21f
            rank < 100 -> 17f
            else -> 12f
        }

        // 값 적용
        tvGroupName.text = groupName
        tvTotalScore.text = "${formattedTotalScore}점"
        tvUserId.text = myName
        tvMyScore.text = "${formattedMyScore}점"
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

    private fun addRandomFeeds(view: View, kcalBurned: Int) {
        val container = view.findViewById<LinearLayout>(R.id.infoContainer)

        // 피드 레이아웃 리스트
        val feedLayouts = mutableListOf(
            R.layout.view_home_tier,
            R.layout.view_home_weekly,
            R.layout.view_home_contribution,
            R.layout.view_home_ranking
        )
        if (kcalBurned > 0) {
            feedLayouts.add(R.layout.view_home_calorie)
        }

        // 섞기
        feedLayouts.shuffle()

        // 레이아웃 인플레이트해서 동적 추가
        val inflater = LayoutInflater.from(view.context)
        for (layoutRes in feedLayouts) {
            val feedView = inflater.inflate(layoutRes, container, false)
            container.addView(feedView)
        }

        // 값 반영
        feedLayouts.forEach { layoutRes ->
            when (layoutRes) {
                R.layout.view_home_calorie -> updateCalorieFeed(view, kcalBurned)
                R.layout.view_home_tier -> updateTierFeed(view, 76)
                R.layout.view_home_weekly -> updateWeeklyFeed(view, listOf(1, 0, 2, 4, 3, 4, 1))
                R.layout.view_home_contribution -> updateContributionFeed(
                    view, 7, "동국대학교", 54082, "컴공이", 432
                )
                R.layout.view_home_ranking -> updateRankingFeed(
                    view, "동국대학교", 7, "컴퓨터공학과", 16
                )
            }
        }
    }

    private fun handleFeedClicks(view: View) {
        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // 미션 피드 → HealthFragment
        view.findViewById<View>(R.id.viewMissionFeed)?.setOnClickListener {
            nav?.selectedItemId = R.id.menu_health
        }

        // 기여도 피드 → RankingFragment
        view.findViewById<View>(R.id.viewContributionFeed)?.setOnClickListener {
            nav?.selectedItemId = R.id.menu_ranking
        }

        // 랭킹 피드 → RankingFragment
        view.findViewById<View>(R.id.viewRankingFeed)?.setOnClickListener {
            nav?.selectedItemId = R.id.menu_ranking
        }
    }
}