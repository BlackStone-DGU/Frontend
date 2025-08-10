package com.example.blackstone

import android.Manifest
import HomeFragment
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.blackstone.data.ExerciseRepository
import com.example.blackstone.databinding.ActivityMainBinding
import com.example.blackstone.health.HealthFragment
import com.example.blackstone.pedometer.PedometerManager
import com.example.blackstone.ranking.RankingFragment
import com.example.blackstone.shopping.ShoppingFragment
import com.example.yourapp.ui.mypage.MyPageFragment


class MainActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var binding: ActivityMainBinding

    private var pedometer: PedometerManager? = null
    private var runningIndex: Int = -1
    private var baseRunningKmAtStart: Double = 0.0

    private val requestActivityReco =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) attachPedometer()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTransparentSystemBars()

        selectTab(R.id.menu_home)
        binding.bottomNavigationView.setOnItemSelectedListener {
            selectTab(it.itemId)
            true
        }

        binding.toolbarProfile.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, MyPageFragment())
                .addToBackStack(null)
                .commit()
        }

        // 러닝 운동 인덱스 찾기 (이름에 "러닝" 포함 기준)
        runningIndex = ExerciseRepository.getExercises().indexOfFirst { it.name.contains("러닝") }

        // 권한 확인/요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_GRANTED -> attachPedometer()
                else -> requestActivityReco.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        } else {
            attachPedometer()
        }
    }

    @Suppress("DEPRECATION", "NewApi")
    private fun setTransparentSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)

            window.insetsController?.apply {
                setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or
                            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }

            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT

        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    )
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
    }

    private fun attachPedometer() {
        if (runningIndex == -1) return

        pedometer = PedometerManager(this)
        // 세션 시작 시점의 러닝 km를 베이스로 저장
        baseRunningKmAtStart = ExerciseRepository.getExercises()[runningIndex].current.toDouble()

        lifecycle.addObserver(pedometer!!)

        pedometer?.onDistanceUpdate = { sessionKm, _ ->
            val newCurrentKm = (baseRunningKmAtStart + sessionKm).toFloat()
            ExerciseRepository.updateCurrent(runningIndex, newCurrentKm)

            val bundle = Bundle().apply { putInt("exerciseId", runningIndex) }
            supportFragmentManager.setFragmentResult("exerciseUpdated", bundle)
        }
    }

    private fun selectTab(menuId: Int) {
        val fragment = when (menuId) {
            R.id.menu_home -> HomeFragment()
            R.id.menu_health -> HealthFragment()
            R.id.menu_ranking -> RankingFragment()
            R.id.menu_shopping -> ShoppingFragment()
            else -> HomeFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        pedometer?.let { lifecycle.removeObserver(it) }
        pedometer = null
    }
}