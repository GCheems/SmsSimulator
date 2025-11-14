package com.example.smssimulator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

/**
 * Prototype receiver that just logs inbound SMS to avoid crashes or lost data.
 */
class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Telephony.Sms.Intents.SMS_DELIVER_ACTION) {
            return
        }
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (sms in messages) {
            Log.d(
                "SmsReceiver",
                "收到真实短信: ${sms.originatingAddress} - ${sms.messageBody}"
            )
        }
    }
}
