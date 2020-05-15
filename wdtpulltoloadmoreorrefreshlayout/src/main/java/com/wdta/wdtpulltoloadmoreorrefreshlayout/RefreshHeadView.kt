package com.wdta.wdtpulltoloadmoreorrefreshlayout

import android.view.View

/**
 * 下拉刷新View
 * 繼承方法
 */
interface RefreshHeadView {
    /**
     * 開始向下拖曳
     */
    fun dragBegin()
    /**
     * 拖曳同時的回調
     * @param progress 拖曳進度
     * @param viewHeight RefreshHeadView 高度
     */
    fun dragProgress(progress:Float,viewHeight:Float)
    /**
     * 拖曳放開後的彈回動作回條
     * @param progress 拖曳進度
     * @param viewHeight RefreshHeadView 高度
     */
    fun dragFinish(progress:Float,viewHeight:Float)
    /**
     * 開始讀取
     */
    fun loading()
    /**
     * 結束刷新
     */
    fun finishLoading()
    /**
     * 取得View
     */
    fun getRefreshHeadView(): View?
}