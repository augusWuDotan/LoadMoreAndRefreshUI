package com.wdta.wdtpulltoloadmoreorrefreshlayout

import android.content.Context

class DisplayUtils {

    companion object {
        val instance = DisplayUtilsHolder.displayUtilsManager
    }

    private object DisplayUtilsHolder {
        val displayUtilsManager = DisplayUtils()
    }

    //dp to px
    fun dp2Px(context: Context, dp: Float): Int {
        return (dp * context.resources.displayMetrics.densityDpi + 0.5F).toInt()
    }

    //px to dp
    fun px2Dp(context: Context, px: Float): Int {
        return (px / context.resources.displayMetrics.densityDpi + 0.5F).toInt()
    }

    //sp to px
    fun sp2Px(context: Context, spValue: Float): Int {
        return (spValue * context.resources.displayMetrics.scaledDensity + 0.5F).toInt()
    }
}