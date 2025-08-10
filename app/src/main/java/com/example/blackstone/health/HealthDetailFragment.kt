package com.example.blackstone.health

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.example.blackstone.data.Exercise
import com.example.blackstone.data.ExerciseRepository

class HealthDetailFragment : Fragment() {

    private var latestCount: Int = 0
    private lateinit var exercise: Exercise
    private lateinit var clearBox: View
    private lateinit var btnConfirmClear: View
    private var exerciseIndex: Int = -1
    private var baseCurrentAtStart: Float = 0f

    companion object {
        private const val ARG_EXERCISE_ID = "arg_exercise_id"

        fun newInstance(exerciseId: Int): HealthDetailFragment {
            val fragment = HealthDetailFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_EXERCISE_ID, exerciseId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exerciseIndex = arguments?.getInt(ARG_EXERCISE_ID) ?: -1
        exercise = ExerciseRepository.getExercise(exerciseIndex) ?: error("잘못된 인덱스")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_health_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ivMission = view.findViewById<ImageView>(R.id.ivMission)
        val tvCount = view.findViewById<TextView>(R.id.tvExerciseCount)
        val tvTitle = view.findViewById<TextView>(R.id.tvHeaderTitle)
        val tvBack = view.findViewById<TextView>(R.id.tvBack)

        val btnRecord = view.findViewById<Button>(R.id.btnRecord)
        val warningBox = view.findViewById<View>(R.id.warningBox)
        val btnConfirmWarning = view.findViewById<TextView>(R.id.btnConfirmWarning)

        // 완료창과 버튼 참조
        clearBox = view.findViewById(R.id.clearBox)
        btnConfirmClear = view.findViewById(R.id.btnConfirmClear)

        // 처음에는 완료창을 감춰 둡니다.
        clearBox.visibility = View.GONE

        // 확인 버튼을 누르면 완료창이 사라지도록 설정
        btnConfirmClear.setOnClickListener {
            clearBox.visibility = View.GONE
        }

        ivMission.setImageResource(exercise.imageResId)

        tvBack.setOnClickListener {
            val result = Bundle().apply {
                putFloat("updatedCurrent", latestCount.toFloat()) // 최신 반복 횟수
            }
            parentFragmentManager.setFragmentResult("squatUpdated", result)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val isRunning = exercise.name.contains("러닝")

        if (isRunning) {
            btnRecord.visibility = View.GONE
            warningBox.visibility = View.GONE

            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerInner, RunningMapFragment())
                .commit()
        } else {
            btnRecord.setOnClickListener {
                warningBox.visibility = View.VISIBLE
                btnRecord.visibility = View.GONE
            }

            btnConfirmWarning.setOnClickListener {
                warningBox.visibility = View.GONE

                baseCurrentAtStart = ExerciseRepository.getExercise(exerciseIndex)?.current ?: exercise.current

                val mc = MotionCaptureFragment().apply {
                    arguments = Bundle().apply {
                        putString("ex_name", mapExerciseKey(exercise.name))
                    }
                }

                childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerInner, mc)
                    .commit()

                clearBox.bringToFront()
                clearBox.parent?.let { (it as View).invalidate() }
            }
        }

        parentFragmentManager.setFragmentResultListener("exerciseUpdated", viewLifecycleOwner) { _, b ->
            val id = b.getInt("exerciseId", -1)
            if (id == exerciseIndex) {
                val updated = ExerciseRepository.getExercise(exerciseIndex) ?: return@setFragmentResultListener
                view.findViewById<TextView>(R.id.tvExerciseCount)?.let { tv ->
                    updateExerciseCount(tv, updated.current, updated.goal, updated.unit)
                }
                checkAndShowCompletion(updated.current)
            }
        }


        // 운동 이름 상단 바에 표시
        tvTitle.text = exercise.name

        updateExerciseCount(tvCount, exercise.current, exercise.goal, exercise.unit)
        checkAndShowCompletion(exercise.current)


        childFragmentManager.setFragmentResultListener("repetitionResult", viewLifecycleOwner) { _, bundle ->
            val delta = bundle.getInt("repetitionDelta", 0)
            if (delta != 0) {
                // 현재 저장된 값에 델타 누적
                val currentStored = ExerciseRepository.getExercise(exerciseIndex)?.current ?: exercise.current
                val newCurrent = currentStored + delta

                ExerciseRepository.updateCurrent(exerciseIndex, newCurrent)
                latestCount = (latestCount + delta) // 화면 복귀 시 재표시용 내부 캐시도 증가

                // UI 갱신
                view.findViewById<TextView>(R.id.tvExerciseCount)?.let { tv ->
                    updateExerciseCount(tv, newCurrent, exercise.goal, exercise.unit)
                }
                checkAndShowCompletion(newCurrent)
                if (clearBox.visibility == View.VISIBLE) {
                    clearBox.bringToFront()
                }
            }
        }

    }

    private fun mapExerciseKey(name: String): String {
        return when {
            name.contains("스쿼트") -> "squat"
            name.contains("푸시업")  -> "pushUp"
            name.contains("플랭크") -> "plank"
            else -> "plank" // 기본값
        }
    }

    private fun checkAndShowCompletion(current: Float) {
        val epsilon = 1e-3f
        val reached = current + epsilon >= exercise.goal

        if (reached) {
            clearBox.visibility = View.VISIBLE
            clearBox.bringToFront()
            clearBox.parent?.let { (it as View).invalidate() }
            clearBox.requestLayout()
        } else {
            clearBox.visibility = View.GONE
        }
    }


    private fun updateExerciseCount(tv: TextView, current: Float, goal: Float, unit: String) {
        val (displayCurrent, displayGoal, displayUnit) =
            if (unit == "km") {
                val curM = (current * 1000f).toInt()
                val goalM = (goal * 1000f).toInt()
                Triple(curM.toString(), goalM.toString(), "m")
            } else {
                val curStr = current.toInt().toString()
                val goalStr = goal.toInt().toString()
                Triple(curStr, goalStr, unit)
            }

        val fullText = "$displayCurrent/$displayGoal$displayUnit"

        val spannable = SpannableString(fullText)
        val unitStart = fullText.indexOf(displayUnit)
        spannable.setSpan(
            AbsoluteSizeSpan(30, true),
            unitStart,
            fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv.text = spannable
    }

    override fun onResume() {
        super.onResume()

        view?.findViewById<TextView>(R.id.tvExerciseCount)?.let { tv ->
            if (latestCount > 0) {
                updateExerciseCount(tv, latestCount.toFloat(), exercise.goal, exercise.unit)
            }
        }
    }
}