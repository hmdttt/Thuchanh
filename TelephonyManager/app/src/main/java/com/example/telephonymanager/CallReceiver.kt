package com.example.telephonymanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                Log.d("CallReceiver", "Incoming call from $incomingNumber")
            }

            if (state == TelephonyManager.EXTRA_STATE_RINGING){
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                if (incomingNumber != null){
                    sendSMS(context, incomingNumber, "I am busy right now. I will call you later.")
                }
            }
        }
    }

    private fun sendSMS(context: Context?, phoneNumber: String, s: String) {
        try {
            val smsManager = android.telephony.SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, s, null, null)
            Log.d("CallReceiver", "SMS sent to $phoneNumber")
        } catch (e: Exception) {
            Log.e("CallReceiver", "Failed to send SMS to $phoneNumber")
        }

    }
}