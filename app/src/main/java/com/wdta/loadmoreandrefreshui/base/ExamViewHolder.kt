package com.wdta.loadmoreandrefreshui.base

import android.content.Context
import android.view.View


/**
 * @author augus
 * @create 2020-03-28
 * @Describe
 */
class ExamViewHolder(itemView: View) : BaseViewHolder<Any>(itemView) {

//    var textView: TextView

    // initializer block //初始化
    init {
//        textView = itemView.findViewById(R.id.textView)
    }

    override fun getClickViews(): ArrayList<View>? {
//        var views = ArrayList<View>()
//        views.add(textView)
        return ArrayList()
    }

    override fun getLongClickViews(): ArrayList<View>? {
        return ArrayList()
    }

    override fun bindViewData(context: Context, data: Any, itemPosition: Int) {
        //as? 回傳 正確轉型或是null
//        val exam = data as? Exam
//        if (exam == null) {
//            return
//        }
//        Log.d(context.packageName, "data ${(data as Exam).toString()}")
    }
}