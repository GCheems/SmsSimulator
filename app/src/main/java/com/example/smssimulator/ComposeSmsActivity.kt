package com.example.smssimulator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Minimal compose activity that receives SEND/SENDTO intents required for ROLE_SMS
 * and forwards the parsed address/body into the main UI.
 */
class ComposeSmsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val forwardIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        intent?.let { source ->
            extractAddress(source.data)?.let { address ->
                forwardIntent.putExtra(EXTRA_ADDRESS, address)
            }
            extractBody(source)?.let { body ->
                forwardIntent.putExtra(EXTRA_BODY, body)
            }
        }

        startActivity(forwardIntent)
        finish()
    }

    private fun extractAddress(uri: Uri?): String? {
        if (uri == null) return null
        val scheme = uri.scheme?.lowercase()
        if (scheme != "sms" && scheme != "smsto" && scheme != "mms" && scheme != "mmsto") {
            return null
        }
        return uri.schemeSpecificPart
    }

    private fun extractBody(source: Intent): String? {
        val directExtra = source.getStringExtra("sms_body")
        if (!directExtra.isNullOrBlank()) {
            return directExtra
        }
        val textExtra = source.getStringExtra(Intent.EXTRA_TEXT)
        if (!textExtra.isNullOrBlank()) {
            return textExtra
        }
        return null
    }

    companion object {
        const val EXTRA_ADDRESS = "compose_address"
        const val EXTRA_BODY = "compose_body"
    }
}
