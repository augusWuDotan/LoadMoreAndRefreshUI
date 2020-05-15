package com.wdta.loadmoreandrefreshui.base

import android.content.Context


/**
 * @author augus
 * @create 2020-03-28
 * @Describe
 */
class UtilAdapter(
    override var context: Context,
    override var mData: ArrayList<Any>? = null
) : BaseAdapter(context, mData) {

    override fun itemHeadViewType(): Int {
        return 0
    }

    override fun itemFootViewType(): Int {
        return 0
    }
}