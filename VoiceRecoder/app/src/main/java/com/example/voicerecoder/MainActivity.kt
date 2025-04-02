package com.example.voicerecoder

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.voicerecoder.ui.theme.VoiceRecoderTheme
import java.io.File
import java.io.IOException

class MainActivity : ComponentActivity() {

    private lateinit var btnRecord: Button
    private lateinit var btnStop: Button
    private lateinit var btnPlay: Button
    private lateinit var listView: ListView
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFilePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRecord = findViewById(R.id.btnRecord)
        btnStop = findViewById(R.id.btnStop)
        btnPlay = findViewById(R.id.btnPlay)
        listView = findViewById(R.id.listView)

        btnRecord.setOnClickListener { startRecording() }
        btnStop.setOnClickListener { stopRecording() }
        btnPlay.setOnClickListener { playRecording() }

        requestPermissions()
        loadRecordings()
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
           Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_AUDIO
        )

        if (permissions.any {
                checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
            }) {
            requestPermissionsLauncher.launch(permissions)
        }
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (!permissions.values.all { it }) {
                Toast.makeText(this, "Cần cấp quyền để sử dụng ứng dụng!", Toast.LENGTH_LONG).show()
            }
        }
    private fun startRecording() {
        val audioFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "record_${System.currentTimeMillis()}.mp3")
        audioFilePath = audioFile.absolutePath

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFilePath)

            try {
                prepare()
                start()
                Toast.makeText(this@MainActivity, "Đang ghi âm...", Toast.LENGTH_SHORT).show()
                btnRecord.isEnabled = false
                btnStop.visibility = Button.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null

        saveRecordingToMediaStore()
        btnRecord.isEnabled = true
        btnStop.visibility = Button.GONE
        Toast.makeText(this, "Ghi âm đã lưu!", Toast.LENGTH_SHORT).show()

        loadRecordings()
    }

    private fun saveRecordingToMediaStore() {
        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "record_${System.currentTimeMillis()}.mp3")
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
            put(MediaStore.Audio.Media.DATA, audioFilePath)
        }
        contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun playRecording() {
        if (audioFilePath.isNotEmpty()) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioFilePath)
                prepare()
                start()
                Toast.makeText(this@MainActivity, "Đang phát...", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loadRecordings() {
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA)
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Audio.Media.DATE_ADDED + " DESC"
        )

        val recordings = mutableListOf<Pair<String, String>>()
        cursor?.use {
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (it.moveToNext()) {
                val name = it.getString(nameColumn)
                val path = it.getString(dataColumn)
                recordings.add(Pair(name, path))
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recordings.map { it.first })
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val filePath = recordings[position].second
            playAudio(filePath)
        }
    }

    private fun playAudio(filePath: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
            start()
            Toast.makeText(this@MainActivity, "Đang phát: $filePath", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
        mediaPlayer?.release()
    }

}

