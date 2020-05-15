package com.wdta.loadmoreandrefreshui.base

import android.content.Context
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author augus
 * @create 2020-03-28
 * @Describe
 */
abstract class BaseViewHolder<Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mViews: SparseArray<View> = SparseArray()
    private var mItemView: View? = itemView

    /**
     * 單擊事件
     */
    abstract fun getClickViews(): ArrayList<View>?

    /**
     * 長鴨事件
     */
    abstract fun getLongClickViews(): ArrayList<View>?

    /**
     * 綁定資料處理事件
     */
    abstract fun bindViewData(context: Context, data: Any, itemPosition: Int)

    /**
     * 綁定最後一個item
     */
    open fun bindFinalItem(context: Context, data: Any, itemPosition: Int){

    }
}