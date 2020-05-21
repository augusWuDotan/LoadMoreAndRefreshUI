package com.wdta.wdtpulltoloadmoreorrefreshlayout

import android.view.View

interface WDTPullToRefreshHeadView {

    /**
     * 開始下拉
     */
    fun begin()

    /**
     *回調的精度,單位為px
     *
     * @param progress 當前高度
     * @param all      總高度 為默認高度的2倍
     */
    fun dragProgress(progress: Float, all: Float)

    fun dragFinishing(progress: Float, all: Float)

    /**
     * 下拉完畢
     */
    fun loading()

    /**
     * 結束
     */
    fun finish()

    /**
     * 返回當前視圖
     */
    fun getFootView(): View?

}