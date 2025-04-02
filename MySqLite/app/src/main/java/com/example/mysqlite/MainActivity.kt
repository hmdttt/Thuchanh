package com.example.mysqlite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var sqLiteHelper: SqLiteHelper
    private lateinit var btnadd : Button
    private lateinit var btnshow : Button
    private lateinit var btndelete : Button
    private lateinit var btnupdate : Button
    private lateinit var edtname: EditText
    private lateinit var edtphone: EditText
    private lateinit var edtid: EditText
    private lateinit var listview: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var contactList = mutableListOf<Triple<Int, String, String>>() // id, name, phone

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnadd = findViewById(R.id.btnAdd)
        btnshow = findViewById(R.id.btnShow)
        btndelete = findViewById(R.id.btnDelete)
        btnupdate = findViewById(R.id.btnUpdate)
        edtname = findViewById(R.id.edtName)
        edtphone = findViewById(R.id.edtPhone)
        edtid = findViewById(R.id.edtID)
        listview = findViewById(R.id.Listview)

        sqLiteHelper = SqLiteHelper(this)

        btnadd.setOnClickListener {
            val name = edtname.text.toString()
            val phone = edtphone.text.toString()
            sqLiteHelper.addContact(name, phone)
            Toast.makeText(this, "Da them", Toast.LENGTH_SHORT).show()
        }
        fun showContacts() {
            contactList = sqLiteHelper.getAllContacts().toMutableList()
            val displayList = contactList.map { "${it.first}:${it.second}: ${it.third}" }
            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList) // android.R.layout.simple_list_item_1 is a built-in layout provided by Android)
            listview.adapter = adapter
        }
        btnshow.setOnClickListener(){
            showContacts()
        }

        btndelete.setOnClickListener(){
            val idText = edtid.text.toString().trim() // Xóa khoảng trắng thừa

            if (idText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập ID!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val id = idText.toInt() // Chuyển đổi an toàn
                val result = sqLiteHelper.deleteContact(id)

                if (result > 0) {
                    Toast.makeText(this, "Xóa liên hệ thành công!", Toast.LENGTH_SHORT).show()
                    showContacts()
                } else {
                    Toast.makeText(this, "Không tìm thấy ID để xóa!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "ID không hợp lệ! Vui lòng nhập số.", Toast.LENGTH_SHORT).show()
            }


        }
        btnupdate.setOnClickListener(){
            val idText = edtid.text.toString().trim()
            val name = edtname.text.toString()
            val phone = edtphone.text.toString()

            if (idText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập ID!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val id = idText.toInt()
                val result = sqLiteHelper.updateContact(id, name, phone)

                if (result > 0) {
                    Toast.makeText(this, "Cập nhật liên hệ thành công!", Toast.LENGTH_SHORT).show()
                    showContacts()
                } else {
                    Toast.makeText(this, "Không tìm thấy ID để cập nhật!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "ID không hợp lệ! Vui lòng nhập số.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}