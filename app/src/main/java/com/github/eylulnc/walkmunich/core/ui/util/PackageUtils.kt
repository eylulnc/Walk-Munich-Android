package com.github.eylulnc.walkmunich.core.ui.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

data class AppVersion(
    val versionName: String,
    val versionCode: Long
)

fun Context.getAppVersion(): AppVersion {
    return try {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        val versionName = packageInfo.versionName ?: "Unknown"
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
        AppVersion(versionName, versionCode)
    } catch (e: PackageManager.NameNotFoundException) {
        AppVersion("Unknown", 0L)
    }
}
