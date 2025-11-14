package com.example.smssimulator

import android.Manifest
import android.app.Activity
import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var requestRoleLauncher: ActivityResultLauncher<Intent>
    private lateinit var tvStatus: TextView
    private lateinit var btnRequestDefault: Button
    private lateinit var etSender: EditText
    private lateinit var etBody: EditText
    private lateinit var btnWriteSms: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        btnRequestDefault = findViewById(R.id.btnRequestDefault)
        etSender = findViewById(R.id.etSender)
        etBody = findViewById(R.id.etBody)
        btnWriteSms = findViewById(R.id.btnWriteSms)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                val allGranted = it.values.all { granted -> granted }
                if (allGranted) {
                    Log.d("MainActivity", "权限已授予")
                } else {
                    Toast.makeText(
                        this,
                        "必须授予所有短信权限才能使用！",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        requestRoleLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "已设置为默认短信应用", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "未能成为默认短信应用", Toast.LENGTH_SHORT).show()
                }
                updateStatus()
            }

        checkRuntimePermissions()

        btnRequestDefault.setOnClickListener {
            requestDefaultSmsRole()
        }

        btnWriteSms.setOnClickListener {
            val sender = etSender.text.toString()
            val body = etBody.text.toString()
            if (SmsUtils.isDefaultSmsApp(this)) {
                SmsUtils.writeFakeSms(this, sender, body)
                Toast.makeText(this, "写入成功！", Toast.LENGTH_SHORT).show()
                showRestoreDefaultDialog()
            } else {
                Toast.makeText(this, "失败：请先将本应用设为默认", Toast.LENGTH_SHORT).show()
            }
        }

        updateStatus()
        populateFieldsFromIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            setIntent(it)
            populateFieldsFromIntent(it)
        }
    }

    private fun updateStatus() {
        if (::tvStatus.isInitialized) {
            val text = if (SmsUtils.isDefaultSmsApp(this)) {
                "Status: 是默认应用"
            } else {
                "Status: 不是默认应用"
            }
            tvStatus.text = text
        }
    }

    private fun populateFieldsFromIntent(intent: Intent) {
        val sender = intent.getStringExtra(ComposeSmsActivity.EXTRA_ADDRESS)
        if (!sender.isNullOrEmpty()) {
            etSender.setText(sender)
        }
        val body = intent.getStringExtra(ComposeSmsActivity.EXTRA_BODY)
        if (!body.isNullOrEmpty()) {
            etBody.setText(body)
        }
    }

    private fun checkRuntimePermissions() {
        val permissionsToRequest = arrayOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
            PERMISSION_WRITE_SMS
        )
        permissionLauncher.launch(permissionsToRequest)
    }

    private fun requestDefaultSmsRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)
            if (roleManager == null || !roleManager.isRoleAvailable(RoleManager.ROLE_SMS)) {
                Toast.makeText(this, "此设备不支持切换默认短信应用", Toast.LENGTH_SHORT).show()
                return
            }
            if (roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                Toast.makeText(this, "已经是默认短信应用", Toast.LENGTH_SHORT).show()
                return
            }
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
            requestRoleLauncher.launch(intent)
        } else {
            SmsUtils.requestChangeDefaultSmsApp(this)
        }
    }

    private fun showRestoreDefaultDialog() {
        AlertDialog.Builder(this)
            .setTitle("温馨提示")
            .setMessage("短信写入完成，为了避免影响正常收发短信，建议现在就切换回原来的默认短信应用。")
            .setPositiveButton("立即更换") { _, _ ->
                SmsUtils.openDefaultSmsSettings(this)
            }
            .setNegativeButton("稍后再说", null)
            .show()
    }

    companion object {
        private const val PERMISSION_WRITE_SMS = "android.permission.WRITE_SMS"
    }
}
