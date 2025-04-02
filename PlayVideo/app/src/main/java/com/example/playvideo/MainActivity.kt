package com.example.playvideo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultCallback
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.playvideo.ui.theme.PlayVideoTheme

class MainActivity : ComponentActivity() {

    private lateinit var videoView: VideoView
    private lateinit var btnChooseVideo: Button
    private lateinit var btnPlayUrl: Button
    private lateinit var edtVideoUrl: EditText

    private lateinit var pickVideoLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        btnChooseVideo = findViewById(R.id.btnChooseVideo)
        btnPlayUrl = findViewById(R.id.btnPlayUrl)
        edtVideoUrl = findViewById(R.id.edtVideoUrl)

        // Đăng ký ActivityResultLauncher cho việc chọn video
        pickVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedVideoUri = result.data?.data
                selectedVideoUri?.let { playVideo(it) }
            }
        }

        // Chọn video từ thiết bị
        btnChooseVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            pickVideoLauncher.launch(intent)
        }

        // Phát video từ URL nhập vào
        btnPlayUrl.setOnClickListener {
            val videoUrl = edtVideoUrl.text.toString()
            if (videoUrl.isNotEmpty()) {
                playVideo(Uri.parse(videoUrl)) // Giả sử playVideo nhận Uri
            } else {
                Toast.makeText(this, "Vui lòng nhập URL hợp lệ!", Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý lỗi khi phát video
        videoView.setOnErrorListener { _, _, _ ->
            Toast.makeText(this, "Lỗi khi phát video!", Toast.LENGTH_SHORT).show()
            true
        }
    }

    // Phương thức phát video
    private fun playVideo(uri: Uri) {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.start()
    }

    // Dừng video khi Activity bị tạm dừng
    override fun onPause() {
        super.onPause()
        if (videoView.isPlaying) {
            videoView.pause()
        }
    }
}
