package net.dfinn.imagewire.util

import android.util.Log
import javax.inject.Inject

class LogCatLogger @Inject constructor() : Logger {
    companion object {
        private const val TAG = "ImageWire"
    }

    override fun logInfo(message: String) {
        Log.i(TAG, message)
    }

    override fun logError(message: String, error: Throwable?) {
        Log.e(TAG, message, error)
    }
}