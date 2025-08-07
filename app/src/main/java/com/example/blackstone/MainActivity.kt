package com.example.blackstone

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.example.blackstone.databinding.ActivityMainBinding
import com.example.blackstone.health.MotionCaptureFragment
import HomeFragment
import com.example.blackstone.health.HealthFragment
import com.example.blackstone.ranking.RankingFragment
import com.example.blackstone.shopping.ShoppingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
}