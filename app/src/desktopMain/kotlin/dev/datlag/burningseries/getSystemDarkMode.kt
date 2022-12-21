package dev.datlag.burningseries

import androidx.compose.runtime.Composable
import dev.datlag.burningseries.other.Constants
import evalBash

@Composable
actual fun getSystemDarkMode(): Boolean {
    val linuxDarkMode = (Constants.LINUX_DARK_MODE_CMD.evalBash().getOrDefault(String())).ifEmpty {
        Constants.LINUX_DARK_MODE_LEGACY_CMD.evalBash().getOrDefault(String())
    }.contains("dark", true)

    return linuxDarkMode
}