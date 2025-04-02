package com.example.installpicture

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
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
import com.example.installpicture.ui.theme.InstallPictureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtUrl = findViewById<EditText>(R.id.edtUrl)
        val btnDownload = findViewById<Button>(R.id.btnDownload)
        val imgView = findViewById<ImageView>(R.id.imgView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        btnDownload.setOnClickListener {
            val url = edtUrl.text.toString().trim()

            if (url.isNotEmpty()) {
                // Tạo AsyncTask và thực thi
                DownloadImageTask(imgView, progressBar).execute(url)
            } else {
                edtUrl.error = "Vui lòng nhập URL hợp lệ!"
            }
        }
    }
}

