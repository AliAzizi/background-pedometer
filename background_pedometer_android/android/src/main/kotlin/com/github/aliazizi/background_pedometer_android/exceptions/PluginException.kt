package com.github.aliazizi.background_pedometer_android.exceptions

abstract class PluginException(
    val code: String, override val message: String, val details: Any? = null
) : RuntimeException(message)

class MissingNotificationDrawableException(iconName: String) : PluginException(
    code = "DRAWABLE_NOT_FOUND",
    message = "Notification drawable resource '$iconName' not found in the application's resources.",
    details = mapOf("icon" to iconName)
)