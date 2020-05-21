package com.wdta.wdtpulltoloadmoreorrefreshlayout.default_view

import android.content.Context
import android.os.UserManager
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.textview.MaterialTextView
import com.wdta.wdtpulltoloadmoreorrefreshlayout.R
import com.wdta.wdtpulltoloadmoreorrefreshlayout.WDTPullToRefreshFootView

class FootLoadMoreView : FrameLayout, WDTPullToRefreshFootView {

    private var mDefaultRefreshHeadView: View? = null
    private var tvVdrhvLoadState: MaterialTextView? = null
    private var tvVdrhvRefreshTime: MaterialTextView? = null
    private var updateType: String? = ""

    init {
        mDefaultRefreshHeadView = LayoutInflater.from(context).inflate(R.layout.view_default_refeash_head_view, null)
        tvVdrhvLoadState = mDefaultRefreshHeadView?.findViewById(R.id.tvVdrhvLoadState)
        tvVdrhvRefreshTime = mDefaultRefreshHeadView?.findViewById(R.id.tvVdrhvRefreshTime)
        addView(mDefaultRefreshHeadView)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun begin() {
        tvVdrhvLoadState?.text = resources.getString(R.string.down_to_refresh)
        //send , receive , exchange
//        val updateTime =
//            when (updateType) {
//                "send" -> {
//                    resources.getString(
//                        R.string.latest_update,
//                        DateUtils.instance.longConvertToString(UserManager.instance.managerSendRedEnvelopeListUpdateTime, "HH：mm")
//                    )
//                }
//                "receive" -> {
//                    resources.getString(
//                        R.string.latest_update,
//                        DateUtils.instance.longConvertToString(
//                            UserManager.instance.memberReceiveRedEnvelopeListUpdateTime,
//                            "HH：mm"
//                        )
//                    )
//                }
//                "exchange" -> {
//                    resources.getString(
//                        R.string.latest_update,
//                        DateUtils.instance.longConvertToString(UserManager.instance.memberExchangeListUpdateTime, "HH：mm")
//                    )
//                }
//                else -> {
//                    resources.getString(
//                        R.string.latest_update,
//                        DateUtils.instance.longConvertToString(UserManager.instance.managerSendRedEnvelopeListUpdateTime, "HH：mm")
//                    )
//                }
//            }
//        tvVdrhvRefreshTime?.text = updateTime
    }

    override fun dragProgress(progress: Float, all: Float) {
        if (progress >= all - 10) {
            tvVdrhvLoadState?.text = resources.getString(R.string.release_to_refresh_immediately)
        }
    }

    override fun dragFinishing(progress: Float, all: Float) {}

    override fun loading() {
        tvVdrhvLoadState?.text = resources.getString(R.string.refreshing_data)
    }

    override fun finish() {
        tvVdrhvLoadState?.text = ""
    }

//    fun setUpdateType(updateType : String){
//        this.updateType = updateType
//    }

    override fun getHeadView(): View? {
        return this
    }

}