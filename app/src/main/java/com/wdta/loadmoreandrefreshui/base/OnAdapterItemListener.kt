package com.wdta.loadmoreandrefreshui.base

import android.view.View

/**
 * @author augus
 * @create 2020-03-28
 * @Describe
 */
interface OnAdapterItemListener {
     fun onItemClick(view: View, viewPosition: Int, itemPositon: Int)

     fun onItemLongClick(view: View, viewPosition: Int, itemPositon: Int)

     fun onItemPositionScroll(viewPosition: Int)
}