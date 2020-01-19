package nl.joozd.logbooktest1.ui

import nl.joozd.logbooktest1.utils.DelegatesExt
import android.app.Application


class App : Application() {
    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
}