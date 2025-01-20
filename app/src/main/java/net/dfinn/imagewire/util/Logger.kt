package net.dfinn.imagewire.util

interface Logger {
    fun logInfo(message: String)
    fun logError(message: String, error: Throwable? = null)
}