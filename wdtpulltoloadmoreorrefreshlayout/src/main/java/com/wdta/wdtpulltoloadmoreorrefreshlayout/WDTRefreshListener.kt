package com.wdta.wdtpulltoloadmoreorrefreshlayout

interface WDTRefreshListener {

    /**
     * 刷新
     */
    fun refresh()

    /**
     * 加载更多
     */
    fun loadMore()
}