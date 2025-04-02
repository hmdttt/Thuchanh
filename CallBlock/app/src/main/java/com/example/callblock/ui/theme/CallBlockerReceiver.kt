package com.example.callblock.ui.theme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class CallBlockerReceiver : BroadcastReceiver (){
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {
                Log.d("CallBlocker", "Cuộc gọi đến từ: $incomingNumber")

                if (isBlockedNumber(incomingNumber, context)) {
                    rejectCall(context)
                }
            }
        }
    }
    private fun isBlockedNumber(phoneNumber: String, context: Context?): Boolean {
        val blockedNumbers = listOf("+84123456789", "0987654321") // Danh sách số bị chặn
        return blockedNumbers.contains(phoneNumber)
    }
    private fun rejectCall(context: Context?) {
        // Chặn cuộc gọi (Chỉ hoạt động trên Android 8.0 trở lên)
        try {
            val telephonyService = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val clazz = Class.forName(telephonyService.javaClass.name)
            val method = clazz.getDeclaredMethod("endCall")
            method.isAccessible = true
            method.invoke(telephonyService)
            Log.d("CallBlocker", "Cuộc gọi bị chặn")
        } catch (e: Exception) {
            Log.e("CallBlocker", "Lỗi chặn cuộc gọi: ${e.message}")
        }
    }
}