package com.wdta.wdtpulltoloadmoreorrefreshlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * @author 吳東承
 * @since 2020.5.15
 *
 * 支持刷新與加載
 */
class RefreshOrLoadMoreLayout : FrameLayout {

    //region attribute
    //此Layout之間的UI
    private var childView: View? = null

    //refreshView
    private var mRefreshView: RefreshHeadView? = null

    //region 刷新項目高度
    private var mRefreshHeadViewHeight =
        RefreshOrLoadMoreLayoutConstants.REFRESH_HEAD_VIEW_DEFAULT_HEIGHT
    private var mRefreshHeadViewDragMaxHeight =
        RefreshOrLoadMoreLayoutConstants.REFRESH_HEAD_VIEW_DEFAULT_DRAG_MAX_HEIGHT
    private var mLoadMoreFootViewHeight =
        RefreshOrLoadMoreLayoutConstants.LOAD_MORE_FOOT_VIEW_DEFAULT_HEIGHT
    private var mLoadMoreFootDragMaxHeight =
        RefreshOrLoadMoreLayoutConstants.LOAD_MORE_FOOT_VIEW_DEFAULT_DRAG_MAX_HEIGHT
    //endregion


    //endregion

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    //region 初始化
    private fun initView() {
        initAttribute()
        checkChildCount()
    }

    //更新屬性
    private fun initAttribute() {
        mRefreshHeadViewHeight = DisplayUtils.instance.dp2Px(context, mRefreshHeadViewHeight.toFloat())
        mRefreshHeadViewDragMaxHeight = DisplayUtils.instance.dp2Px(context, mRefreshHeadViewDragMaxHeight.toFloat())
        mLoadMoreFootViewHeight = DisplayUtils.instance.dp2Px(context, mLoadMoreFootViewHeight.toFloat())
        mLoadMoreFootDragMaxHeight = DisplayUtils.instance.dp2Px(context, mLoadMoreFootDragMaxHeight.toFloat())
    }

    //確定子項目數量
    private fun checkChildCount() {
        val childCount = this.childCount
        //如果子項目不為1 發出 exception
        if (childCount != 1) {
            throw IllegalArgumentException("子項目僅能為一個")
        }
    }
    //endregion

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        childView = getChildAt(0)
        addRefreshHeadView()
        addLoadMoreFootView()
    }

    //region 加入 刷新與加載 View
    //刷新
    private fun addRefreshHeadView() {
        if (mRefreshView == null) {
            mRefreshView = DefaultRefreshHeadView(context)
        }
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0)
        mRefreshView?.getRefreshHeadView()?.layoutParams = layoutParams
        if(mRefreshView?.getRefreshHeadView()?.parent != null){
            (mRefreshView?.getRefreshHeadView()?.parent as ViewGroup).removeAllViews()
        }
        addView(mRefreshView?.getRefreshHeadView(),0)
    }

    //加載
    private fun addLoadMoreFootView() {

    }
    //endregion
}