package nl.joozd.logbooktest1.utils

import android.content.Context

fun startMainActivity(context: Context) = with (context) {
    startActivity(packageManager.getLaunchIntentForPackage(packageName))
}