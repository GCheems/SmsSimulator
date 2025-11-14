package com.example.smssimulator

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.telephony.TelephonyManager
import android.util.Log

/**
 * Handles quick-reply intents such as ACTION_RESPOND_VIA_MESSAGE so the app meets
 * the default SMS role requirements.
 */
class RespondViaMessageService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)
        stopSelfResult(startId)
        return START_NOT_STICKY
    }

    private fun handleIntent(intent: Intent?) {
        val actualIntent = intent ?: return
        if (actualIntent.action != TelephonyManager.ACTION_RESPOND_VIA_MESSAGE) {
            return
        }
        val address = actualIntent.data?.schemeSpecificPart
        val body = actualIntent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString()
        Log.d(TAG, "RespondViaMessageService forwarding reply to UI, address=$address")

        val uiIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            address?.let { putExtra(ComposeSmsActivity.EXTRA_ADDRESS, it) }
            body?.let { putExtra(ComposeSmsActivity.EXTRA_BODY, it) }
        }
        startActivity(uiIntent)
    }

    companion object {
        private const val TAG = "RespondViaMsgService"
    }
}
