package com.wdta.wdtpulltoloadmoreorrefreshlayout

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.google.android.material.textview.MaterialTextView

class DefaultRefreshHeadView @SuppressLint("InflateParams") constructor(context: Context) : FrameLayout(context)
    , RefreshHeadView {

    private var mDefaultRefreshHeadView: View? = null
    private var pbVdrhvLoadState: ProgressBar? = null
    private var tvVdrhvLoadState: MaterialTextView? = null

    init {
        mDefaultRefreshHeadView = LayoutInflater.from(context).inflate(R.layout.view_default_refeash_head_view, this, false)
        tvVdrhvLoadState = mDefaultRefreshHeadView?.findViewById(R.id.tvVdrhvLoadState)
        pbVdrhvLoadState = mDefaultRefreshHeadView?.findViewById(R.id.pbVdrhvLoadState)
        addView(mDefaultRefreshHeadView)
    }


    override fun dragBegin() {
        pbVdrhvLoadState?.isIndeterminate = true
        pbVdrhvLoadState?.interpolator = AccelerateDecelerateInterpolator()
        tvVdrhvLoadState?.text = "下拉加載"
    }

    override fun dragProgress(progress: Float, viewHeight: Float) {
        if (progress >= viewHeight - 10) {
            tvVdrhvLoadState?.text = "放開刷新"
        } else {
            tvVdrhvLoadState?.text = "下拉重新整理"
        }
    }

    override fun dragFinish(progress: Float, viewHeight: Float) {

    }

    override fun loading() {
        tvVdrhvLoadState?.text = "載入中"
    }

    override fun finishLoading() {
        pbVdrhvLoadState?.isIndeterminate = false
        tvVdrhvLoadState?.text = ""
    }

    override fun getRefreshHeadView(): View? {
        return mDefaultRefreshHeadView
    }
}
