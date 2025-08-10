package com.example.blackstone.ranking

import android.content.res.Resources
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.blackstone.R
import com.example.blackstone.databinding.FragmentRankingBinding
import java.text.NumberFormat
import java.util.Locale

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    private lateinit var rankingAdapter: RankingAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isSchoolMode = true // true: 학교, false: 학과
    private var myAffiliationIndex: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupTabSwitch()
        setupMyRankingClick()
    }

    private fun setupRecyclerView() {
        rankingAdapter = RankingAdapter()
        layoutManager = LinearLayoutManager(requireContext())

        binding.rvRanking.layoutManager = layoutManager
        binding.rvRanking.adapter = rankingAdapter

        binding.rvRanking.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastShouldShow = true

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition()
                    .takeIf { it != RecyclerView.NO_POSITION } ?: 0
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()
                    .takeIf { it != RecyclerView.NO_POSITION } ?: 0

                val shouldShow = myAffiliationIndex != -1 && myAffiliationIndex !in firstVisible..lastVisible

                if (shouldShow != lastShouldShow) {
                    setMyRankingVisibility(shouldShow)
                    lastShouldShow = shouldShow
                }
            }
        })

        updateRankingList()
    }

    private fun setMyRankingVisibility(visible: Boolean) {
        val constraintSet = ConstraintSet().apply {
            clone(binding.rankingFragmentRoot)
        }

        if (!visible) {
            TransitionManager.beginDelayedTransition(binding.rankingFragmentRoot)
        }

        if (visible) {
            binding.myRanking.visibility = View.VISIBLE
            constraintSet.constrainHeight(R.id.myRanking, 75.dp)
            constraintSet.connect(
                R.id.rvRanking,
                ConstraintSet.TOP,
                R.id.myRanking,
                ConstraintSet.BOTTOM,
                12.dp
            )

        } else {
            constraintSet.constrainHeight(R.id.myRanking, 0)
            constraintSet.connect(
                R.id.rvRanking,
                ConstraintSet.TOP,
                R.id.llRankingTab,
                ConstraintSet.BOTTOM,
                12.dp
            )
        }

        constraintSet.applyTo(binding.rankingFragmentRoot)
    }

    private fun setMyRankingVisibilityInstantly(visible: Boolean) {
        val constraintSet = ConstraintSet().apply {
            clone(binding.rankingFragmentRoot)
        }

        if (visible) {
            binding.myRanking.visibility = View.VISIBLE
            constraintSet.constrainHeight(R.id.myRanking, 75.dp)
            constraintSet.connect(
                R.id.rvRanking,
                ConstraintSet.TOP,
                R.id.myRanking,
                ConstraintSet.BOTTOM,
                12.dp
            )

        } else {
            constraintSet.constrainHeight(R.id.myRanking, 0)
            constraintSet.connect(
                R.id.rvRanking,
                ConstraintSet.TOP,
                R.id.llRankingTab,
                ConstraintSet.BOTTOM,
                12.dp
            )
        }

        constraintSet.applyTo(binding.rankingFragmentRoot)
    }

    private fun setupMyRankingClick() {
        binding.myRanking.setOnClickListener {
            if (myAffiliationIndex != -1) {
                binding.rvRanking.smoothScrollToPosition(myAffiliationIndex)

                binding.rvRanking.postDelayed({
                    setMyRankingVisibilityInstantly(false)
                }, 50)
            }
        }
    }

    val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun updateMyRankingView(rankingList: List<RankingItem>) {
        val myItem = rankingList.find { it.isMyAffiliation }
        if (myItem != null) {
            binding.myRanking.visibility = View.VISIBLE
            binding.tvRanking.text = "${myItem.rank}위"
            binding.tvName.text = myItem.name
            binding.tvScore.text = "${NumberFormat.getNumberInstance(Locale.US).format(myItem.score)}점"
        } else {
            binding.myRanking.visibility = View.GONE
        }
    }

    private fun setupTabSwitch() {
        val tabSchool = binding.tabSchool
        val tabMajor = binding.tabMajor

        val selectedColor = resources.getColor(R.color.WinnerFit_lightPurple, null)
        val unselectedColor = android.graphics.Color.parseColor("#625987")

        tabSchool.setOnClickListener {
            isSchoolMode = true
            updateRankingList()
            tabSchool.setTextColor(selectedColor)
            tabMajor.setTextColor(unselectedColor)
            binding.rvRanking.scrollToPosition(0)
        }

        tabMajor.setOnClickListener {
            isSchoolMode = false
            updateRankingList()
            tabSchool.setTextColor(unselectedColor)
            tabMajor.setTextColor(selectedColor)
            binding.rvRanking.scrollToPosition(0)
        }

        // 초기 색상 설정
        tabSchool.setTextColor(selectedColor)
        tabMajor.setTextColor(unselectedColor)
    }

    private fun updateRankingList() {
        val rankingList = if (isSchoolMode) {
            listOf(
                RankingItem(1, "연세대학교", 234200),
                RankingItem(2, "경희대학교", 229500),
                RankingItem(3, "고려대학교", 224300),
                RankingItem(4, "성균관대학교", 218900),
                RankingItem(5, "한양대학교", 214000),
                RankingItem(6, "중앙대학교", 209800),
                RankingItem(7, "동국대학교", 206132, isMyAffiliation = true),
                RankingItem(8, "이화여자대학교", 201400),
                RankingItem(9, "한국외국어대학교", 197200),
                RankingItem(10, "서울시립대학교", 193800),
                RankingItem(11, "건국대학교", 190300),
                RankingItem(12, "홍익대학교", 186900),
                RankingItem(13, "서울대학교", 183500),
                RankingItem(14, "숭실대학교", 180100),
                RankingItem(15, "아주대학교", 176800),
                RankingItem(16, "인하대학교", 173200),
                RankingItem(17, "단국대학교", 170400),
                RankingItem(18, "국민대학교", 168000),
                RankingItem(19, "세종대학교", 165300),
                RankingItem(20, "광운대학교", 162800),
                RankingItem(21, "명지대학교", 159700),
                RankingItem(22, "서울과학기술대학교", 157400),
                RankingItem(23, "가톨릭대학교", 154900),
                RankingItem(24, "부산대학교", 152100),
                RankingItem(25, "전남대학교", 149800),
                RankingItem(26, "경북대학교", 147300),
                RankingItem(27, "충남대학교", 144900),
                RankingItem(28, "전북대학교", 142500),
                RankingItem(29, "강원대학교", 140200),
                RankingItem(30, "제주대학교", 137700)
            )
        } else {
            listOf(
                RankingItem(1, "경찰행정학부", 9540),
                RankingItem(2, "정보통신공학과", 9401),
                RankingItem(3, "컴퓨터AI학부", 9310,  isMyAffiliation = true),
                RankingItem(4, "전자전기공학과", 9250),
                RankingItem(5, "철학과", 9160),
                RankingItem(6, "건설환경공학과", 9065),
                RankingItem(7, "화공생물공학과", 8935),
                RankingItem(8, "통계학과", 8801),
                RankingItem(9, "산업시스템공학과", 8700),
                RankingItem(10, "경영학과", 8610),
                RankingItem(11, "경제학과", 8530),
                RankingItem(12, "미디어커뮤니케이션학과", 8408),
                RankingItem(13, "영어영문학과", 8310),
                RankingItem(14, "사학과", 8278),
                RankingItem(15, "국어국문문예창작학부", 8130),
                RankingItem(16, "행정학과", 8040),
                RankingItem(17, "사회복지학과", 7955),
                RankingItem(18, "심리학과", 7870),
                RankingItem(19, "경찰학과", 7788),
                RankingItem(20, "소프트웨어학과", 7700),
                RankingItem(21, "데이터사이언스학과", 7615),
                RankingItem(22, "의생명공학과", 7530),
                RankingItem(23, "기계공학과", 7442),
                RankingItem(24, "건축학과", 7355),
                RankingItem(25, "물리학과", 7260),
                RankingItem(26, "화학과", 7173),
                RankingItem(27, "수학과", 7090),
                RankingItem(28, "국제통상학과", 7008),
                RankingItem(29, "중어중문학과", 6920),
                RankingItem(30, "불어불문학과", 6835)
            )
        }
        myAffiliationIndex = rankingList.indexOfFirst { it.isMyAffiliation }
        updateMyRankingView(rankingList)
        rankingAdapter.setItems(rankingList)


        binding.rvRanking.post {
            val firstVisible = layoutManager.findFirstVisibleItemPosition().takeIf { it != RecyclerView.NO_POSITION } ?: 0
            val lastVisible = layoutManager.findLastVisibleItemPosition().takeIf { it != RecyclerView.NO_POSITION } ?: 0
            val isVisible = myAffiliationIndex in firstVisible..lastVisible
            setMyRankingVisibility(!isVisible)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}