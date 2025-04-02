package com.example.csesport

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.csesport.ui.theme.CSESportTheme
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //tham chieu cac phan tu
        //val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav)
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val frameMain = findViewById<FrameLayout>(R.id.frame_main)
        //sukien
        bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_item_running -> {
                   loadFragment(RunningFragment())
                    true
                }
                R.id.menu_item_cycling ->{
                    loadFragment(CyclingFragment())
                    true
                }
            }
            true
        }
        if(savedInstanceState == null){
            loadFragment(RunningFragment())
        }


    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main, fragment) // Thay thế fragment hiện tại bằng fragment mới
            .commit() // Xác nhận thay đổi

    }
}

