package com.example.smssimulator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class MmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.WAP_PUSH_DELIVER_ACTION) {
            Log.d("MmsReceiver", "收到 MMS PUSH")
        }
    }
}
