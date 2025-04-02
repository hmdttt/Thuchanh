package com.example.mysimplechat.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase

class MessageViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

}