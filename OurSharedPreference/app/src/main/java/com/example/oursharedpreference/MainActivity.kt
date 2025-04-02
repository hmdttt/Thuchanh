package com.example.oursharedpreference


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
import com.example.oursharedpreference.ui.theme.OurSharedPreferenceTheme

class MainActivity : ComponentActivity() {
    private lateinit var edtinput: EditText
    private lateinit var btnsave : Button
    private lateinit var btnload : Button
    private lateinit var tvresult : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //tham chieu
        edtinput = findViewById(R.id.edtInput)
        btnsave = findViewById(R.id.btnSave)
        btnload = findViewById(R.id.btnLoad)
        tvresult = findViewById(R.id.tvResult)

        btnsave.setOnClickListener(){
            val input = edtinput.text.toString()
            val sharedPreferences = getSharedPreferences("ILOVEU", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("PHONEEEE", input)
            editor.apply()
        }
        btnload.setOnClickListener(){
            val sharedPreferences = getSharedPreferences("ILOVEU", MODE_PRIVATE)
            val input = sharedPreferences.getString("PHONEEEE", "")
            tvresult.text = input
        }



    }
}

