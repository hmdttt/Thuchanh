package com.example.coutthetime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.coutthetime.ui.theme.CoutTheTimeTheme
import android.os.Handler
import android.os.Looper
import android.widget.TextView


class MainActivity : ComponentActivity() {

    private lateinit var tvTimer: TextView
    private var seconds = 0
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            seconds++
            tvTimer.text = "$seconds giây"
            handler.postDelayed(this, 1000) // Cập nhật mỗi giây
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)
        handler.post(runnable) // Bắt đầu đếm giờ ngay khi mở app
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // Dừng handler khi Activity bị hủy
    }
}

