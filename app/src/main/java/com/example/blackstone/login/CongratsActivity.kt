package com.example.blackstone.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.blackstone.R
import com.example.blackstone.databinding.ActivityCongratsBinding

class CongratsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCongratsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.WinnerFit_black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.WinnerFit_black)

        binding = ActivityCongratsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}