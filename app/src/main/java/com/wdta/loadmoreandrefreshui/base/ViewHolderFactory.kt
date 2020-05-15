package com.wdta.loadmoreandrefreshui.base

import android.view.View
import com.wdta.loadmoreandrefreshui.base.BaseViewHolder
import com.wdta.loadmoreandrefreshui.base.ViewHolderConstant

/**
 * @author augus
 * @create 2020-03-28
 * @Describe
 */
class ViewHolderFactory {

    fun createViewHolder(type: Int, view: View): BaseViewHolder<Any> {
        when (type) {
            else ->
                return ExamViewHolder(view)
        }
    }
}