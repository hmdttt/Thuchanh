package com.example.installpicture

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(
    private val imageView: ImageView,
    private val progressBar: ProgressBar
) : AsyncTask<String, Int, Bitmap?>() {

    override fun onPreExecute() {
        super.onPreExecute()
        progressBar.visibility = ProgressBar.VISIBLE
        progressBar.progress = 0
    }

    override fun doInBackground(vararg params: String?): Bitmap? {
        val urlString = params[0] ?: return null
        try {
            val url = URL(urlString)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            val totalSize = connection.contentLength
            val inputStream: InputStream = connection.inputStream
            val byteArray = inputStream.readBytes()
            var downloadedSize = 0

            // Giả lập quá trình cập nhật tiến trình
            for (i in byteArray.indices step 4096) { // Tăng dần từng 4KB
                downloadedSize += 4096
                val progress = (downloadedSize * 100 / totalSize)
                publishProgress(progress.coerceAtMost(100)) // Đảm bảo không vượt quá 100%
                Thread.sleep(50) // Mô phỏng thời gian tải
            }

            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        } catch (e: Exception) {
            Log.e("DownloadImageTask", "Lỗi tải ảnh: ${e.message}")
            return null
        }
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        progressBar.progress = values[0] ?: 0
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        progressBar.visibility = ProgressBar.GONE
        if (result != null) {
            imageView.setImageBitmap(result)
        }
    }
}