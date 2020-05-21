package com.wdta.wdtpulltoloadmoreorrefreshlayout

import android.content.Context

class DisplayUtil {
    object cal{
        fun dp2Px(context: Context, dp: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }

        fun px2Dp(context: Context, px: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (px / scale + 0.5f).toInt()
        }

        fun sp2px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }
    }
}