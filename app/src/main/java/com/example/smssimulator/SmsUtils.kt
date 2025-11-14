package com.example.smssimulator

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.provider.Telephony

object SmsUtils {

    fun isDefaultSmsApp(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(RoleManager::class.java)
            if (roleManager?.isRoleAvailable(RoleManager.ROLE_SMS) == true &&
                roleManager.isRoleHeld(RoleManager.ROLE_SMS)
            ) {
                return true
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false
        }

        val defaultPackage = Telephony.Sms.getDefaultSmsPackage(context)
        return context.packageName == defaultPackage
    }

    fun requestChangeDefaultSmsApp(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT).apply {
            putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun openDefaultSmsSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } else {
            requestChangeDefaultSmsApp(context)
        }
    }

    fun writeFakeSms(context: Context, sender: String, body: String) {
        if (!isDefaultSmsApp(context)) {
            android.util.Log.w("SmsUtils", "writeFakeSms aborted: not default SMS app.")
            return
        }
        try {
            val now = System.currentTimeMillis()
            val values = android.content.ContentValues().apply {
                put(Telephony.Sms.Inbox.ADDRESS, sender)
                put(Telephony.Sms.Inbox.BODY, body)
                put(Telephony.Sms.Inbox.DATE, now)
                put(Telephony.Sms.Inbox.DATE_SENT, now)
                put(Telephony.Sms.Inbox.READ, 0)
                put(Telephony.Sms.Inbox.SEEN, 0)
            }
            context.contentResolver.insert(Telephony.Sms.Inbox.CONTENT_URI, values)
            android.util.Log.i("SmsUtils", "Fake SMS inserted for sender=$sender")
        } catch (t: Throwable) {
            android.util.Log.e("SmsUtils", "Failed to insert fake SMS", t)
        }
    }
}
