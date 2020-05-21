package com.wdta.wdtpulltoloadmoreorrefreshlayout

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import com.wdta.wdtpulltoloadmoreorrefreshlayout.default_view.FootLoadMoreView
import com.wdta.wdtpulltoloadmoreorrefreshlayout.default_view.HeadRefreshView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * 此乃極巨光科技公司 建置UI
 * 工程師:吳東承
 * //已將淘汰方法更新
 */
open class WDTPullToRefreshLayout : FrameLayout {

    //region view
    private var mHeaderView: WDTPullToRefreshHeadView? = null
    private var mFooterView: WDTPullToRefreshFootView? = null
    private var mChildView: View? = null
    //endregion

    //region property

    //region 基本高度計算
    private var headHeight: Int = 0
    private var headMaxHeight: Int = 0
    private var footHeight: Int = 0
    private var footMaxHeight: Int = 0

    //endregion

    //region touch
    private var mTouchY: Float = 0f
    private var mCurrentY: Float = 0f

    //移動的最小距離
    private var mTouchSlope: Float = 0f
    //endregion

    //region 基本判斷屬性
    private var canLoadMore: Boolean = true
    private var canRefresh: Boolean = true
    private var isRefresh: Boolean = false
    private var isLoadMore: Boolean = false
    //endregion

    //region listener
    var refreshListener: WDTRefreshListener? = null
    //endregion

    //endregion


    //region constructor
    constructor(context: Context) : super(context, null, 0) {
        Log.d("constructor", "constructor 1")
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        Log.d("constructor", "constructor 2")
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        Log.d("constructor", "constructor 3")
        initView()
    }
    //endregion


    //region 初始化
    private fun initView() {
        cal()
        val count = childCount
        if (count != 1) {
            Log.d("initView", "child only can be one")
        }
    }

    //初始化計算
    private fun cal() {
        headHeight =
            DisplayUtil.cal.dp2Px(context, WDTPullToRefreshConstants.HEAD_HEIGHT.toFloat())
        footHeight =
            DisplayUtil.cal.dp2Px(context, WDTPullToRefreshConstants.FOOT_HEIGHT.toFloat())
        headMaxHeight =
            DisplayUtil.cal.dp2Px(context, WDTPullToRefreshConstants.HEAD_HEIGHT.toFloat() * 2)
        footMaxHeight =
            DisplayUtil.cal.dp2Px(context, WDTPullToRefreshConstants.FOOT_HEIGHT.toFloat() * 2)

        mTouchSlope = ViewConfiguration.get(context).scaledTouchSlop.toFloat()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mChildView = getChildAt(0)
        addHeadView()
        addFooterView()
    }

    private fun addHeadView() {
        if (mHeaderView == null) {
            mHeaderView = HeadRefreshView(context)
        }
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        mHeaderView!!.getFootView()!!.layoutParams = layoutParams
        if (mHeaderView!!.getFootView()!!.parent != null) (mHeaderView!!.getFootView()!!.parent as ViewGroup).removeAllViews()
        addView(mHeaderView!!.getFootView(), 0)
    }

    private fun addFooterView() {
        if (mFooterView == null) {
            mFooterView = FootLoadMoreView(context)
        }
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        layoutParams.gravity = Gravity.BOTTOM
        mFooterView?.getHeadView()?.layoutParams = layoutParams
        if (mFooterView?.getHeadView()?.parent != null) (mFooterView?.getHeadView()?.parent as ViewGroup).removeAllViews()
        addView(mFooterView?.getHeadView())
    }
    //endregion

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!canLoadMore && !canRefresh) return super.onInterceptTouchEvent(ev)
        if (isRefresh || isLoadMore) return true
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchY = ev.y
                mCurrentY = mTouchY
            }
            MotionEvent.ACTION_MOVE -> {
                val currentY = ev.y
                val dy = currentY - mCurrentY
                if (canRefresh) {
                    val canChildScrollUp = canChildScrollUp()
                    if (dy > mTouchSlope && !canChildScrollUp) {
                        mHeaderView!!.begin()
                        return true
                    }
                }
                if (canLoadMore) {
                    val canChildScrollDown = canChildScrollDown()
                    if (dy < -mTouchSlope && !canChildScrollDown) {
                        mFooterView!!.begin()
                        return true
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isRefresh || isLoadMore) return true
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                mCurrentY = event.y
                var dura = (mCurrentY - mTouchY) / 3.0f
                if (dura > 0 && canRefresh) {
                    dura = min(headMaxHeight.toFloat(), dura)
                    dura = max(0f, dura)
                    mHeaderView!!.getFootView()!!.layoutParams.height = dura.toInt()
                    mChildView?.translationY = dura
                    requestLayout()
                    mHeaderView!!.dragProgress(dura, headHeight.toFloat())
                } else {
                    if (canLoadMore) {
                        dura = min(footMaxHeight.toFloat(), abs(dura))
                        dura = max(0f, abs(dura))
                        mFooterView!!.getHeadView()!!.layoutParams.height = dura.toInt()
                        mChildView?.translationY = -dura
                        requestLayout()
                        mFooterView!!.dragProgress(dura, footHeight.toFloat())
                    }
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val currentY = event.y
                val dy = (currentY - mTouchY).toInt() / 3
                if (dy > 0 && canRefresh) {
                    if (dy >= headHeight) {
                        createAnimatorTranslationY(
                            WDTPullToRefreshConstants.REFRESH,
                            if (dy > headMaxHeight) headMaxHeight else dy,
                            headHeight,
                            object : AnimationCallBack {
                                override fun onSuccess() {
                                    isRefresh = true
                                    if (refreshListener != null) {
                                        refreshListener!!.refresh()
                                    }
                                    mHeaderView!!.loading()
                                }
                            })
                    } else if (dy in 1 until headHeight) {
                        setFinish(dy, WDTPullToRefreshConstants.REFRESH)
                        mHeaderView!!.finish()
                    }
                } else {
                    if (canLoadMore) {
                        if (abs(dy) >= footHeight) {
                            createAnimatorTranslationY(
                                WDTPullToRefreshConstants.LOADMORE,
                                if (abs(dy) > footMaxHeight) footMaxHeight else abs(
                                    dy
                                ),
                                footHeight,
                                object : AnimationCallBack {
                                    override fun onSuccess() {
                                        isLoadMore = true
                                        if (refreshListener != null) {
                                            refreshListener!!.loadMore()
                                        }
                                        mFooterView!!.loading()
                                    }
                                })
                        } else {
                            setFinish(abs(dy), WDTPullToRefreshConstants.LOADMORE)
                            mFooterView!!.finish()
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun canChildScrollDown(): Boolean {
        return if (mChildView == null) {
            false
        } else mChildView?.canScrollVertically(1)!!
    }

    private fun canChildScrollUp(): Boolean {
        return if (mChildView == null) {
            false
        } else mChildView?.canScrollVertically(-1)!!
    }

    /**
     * 創建動畫
     */
    private fun createAnimatorTranslationY(
        state: Int,
        start: Int,
        purpose: Int,
        callBack: AnimationCallBack?
    ) {
        val anim: ValueAnimator = ValueAnimator.ofInt(start, purpose)
        anim.duration = WDTPullToRefreshConstants.ANIM_TIME.toLong()
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            if (state == WDTPullToRefreshConstants.REFRESH) {
                mHeaderView!!.getFootView()!!.layoutParams.height = value
                mChildView?.translationY = value.toFloat()
                if (purpose == 0) { //代表结束加载
                    mHeaderView!!.dragFinishing(value.toFloat(), headMaxHeight.toFloat())
                } else {
                    mHeaderView!!.dragProgress(value.toFloat(), headHeight.toFloat())
                }
            } else {
                mFooterView!!.getHeadView()!!.layoutParams.height = value
                mChildView?.translationY = -value.toFloat()
                if (purpose == 0) { //代表结束加载
                    mFooterView!!.dragFinishing(value.toFloat(), headMaxHeight.toFloat())
                } else {
                    mFooterView!!.dragProgress(value.toFloat(), footHeight.toFloat())
                }
            }
            if (value == purpose) {
                callBack?.onSuccess()
            }
            requestLayout()
        }
        anim.start()
    }


    /**
     * 結束下拉刷新
     */
    private fun setFinish(height: Int, state: Int) {
        createAnimatorTranslationY(state, height, 0,
            object : AnimationCallBack {
                override fun onSuccess() {
                    if (state == WDTPullToRefreshConstants.REFRESH) {
                        isRefresh = false
                        mHeaderView!!.finish()
                    } else {
                        isLoadMore = false
                        mFooterView!!.finish()
                    }
                }
            })
    }

    private fun setFinish(state: Int) {
        if (state == WDTPullToRefreshConstants.REFRESH) {
            if (mHeaderView != null && mHeaderView!!.getFootView()!!.layoutParams.height > 0 && isRefresh) {
                setFinish(headHeight, state)
            }
        } else {
            if (mFooterView != null && mFooterView!!.getHeadView()!!.layoutParams.height > 0 && isLoadMore) {
                setFinish(footHeight, state)
            }
        }
    }

    /**
     * 結束刷新
     */
    fun finishRefresh() {
        setFinish(WDTPullToRefreshConstants.REFRESH)
    }

    /**
     * 結束加載更多
     */
    fun finishLoadMore() {
        setFinish(WDTPullToRefreshConstants.LOADMORE)
    }

    //region 設定

    /**
     * 設置是否啟用加載更多
     */
    fun setCanLoadMore(canLoadMore: Boolean) {
        this.canLoadMore = canLoadMore
    }

    /**
     * 設置是否啟用刷新
     */
    fun setCanRefresh(canRefresh: Boolean) {
        this.canRefresh = canRefresh
    }


    /**
     * 設置是拖動刷新右邊
     * @param mHeaderView 需實現HeadView接口
     */
    fun setHeaderView(mHeaderView: WDTPullToRefreshHeadView?) {
        this.mHeaderView = mHeaderView
    }

    /**
     *設置是拖動刷新尾部
     * @param mFooterView 需實現FooterView接口
     */
    fun setFooterView(mFooterView: WDTPullToRefreshFootView?) {
        this.mFooterView = mFooterView
    }


    /**
     *設置刷新控件的高度
     * @param dp 單位為dp
     */
    fun setHeadHeight(dp: Int) {
        headHeight = DisplayUtil.cal.dp2Px(context, dp.toFloat())
    }

    /**
     * 設置加載更多控件的高度
     * @param dp 單位為dp
     */
    fun setFootHeight(dp: Int) {
        footHeight = DisplayUtil.cal.dp2Px(context, dp.toFloat())
    }

    /**
     *同時設置加載更多控件和刷新控件的高度
     * @param dp 單位為dp
     */
    fun setAllHeight(dp: Int) {
        headHeight = DisplayUtil.cal.dp2Px(context, dp.toFloat())
        footHeight = DisplayUtil.cal.dp2Px(context, dp.toFloat())
    }

    /**
     * 同時設置加載更多控件和刷新控件的高度
     * @param refresh  刷新控件的高度單位為dp
     * @param loadMore 加載控件的高度單位為dp
     */
    fun setAllHeight(refresh: Int, loadMore: Int) {
        headHeight =
            DisplayUtil.cal.dp2Px(context, refresh.toFloat())
        footHeight =
            DisplayUtil.cal.dp2Px(context, loadMore.toFloat())
    }

    /**
     * 設置刷新控件的拖動的最大高度且必須大於本身控件的高度最佳為2倍
     * @param dp 單位為dp
     */
    fun setMaxHeadHeight(dp: Int) {
        if (headHeight >= DisplayUtil.cal.dp2Px(
                context,
                dp.toFloat()
            )
        ) {
            return
        }
        headMaxHeight = DisplayUtil.cal.dp2Px(context, dp.toFloat())
    }

    /**
     * 設置加載更多控件的上拉的最大高度且必須大於本身控件的高度最佳為2倍
     * @param dp 單位為dp
     */
    fun setMaxFootHeight(dp: Int) {
        if (footHeight >= DisplayUtil.cal.dp2Px(
                context,
                dp.toFloat()
            )
        ) {
            return
        }
        footMaxHeight = DisplayUtil.cal.dp2Px(context, dp.toFloat())
    }

    /**
     * 同時設置加載更多控件和刷新控件的最大高度且必須大於本身控件的高度最佳為2倍
     * @param dp 單位為dp
     */
    fun setAllMaxHeight(dp: Int) {
        if (headHeight >= DisplayUtil.cal.dp2Px(
                context,
                dp.toFloat()
            )
        ) {
            return
        }
        if (footHeight >= DisplayUtil.cal.dp2Px(
                context,
                dp.toFloat()
            )
        ) {
            return
        }
        headMaxHeight = DisplayUtil.cal.dp2Px(context, dp.toFloat())
        footMaxHeight = DisplayUtil.cal.dp2Px(context, dp.toFloat())
    }

    /**
     * 同時設置加載更多控件和刷新控件的最大高度且必須大於本身控件的高度最佳為2倍
     * @param refresh  刷新控件拖動的最大高度單位為dp
     * @param loadMore 加載控件上拉的最大高度單位為dp
     */
    fun setAllMaxHeight(refresh: Int, loadMore: Int) {
        if (headHeight >= DisplayUtil.cal.dp2Px(
                context,
                refresh.toFloat()
            )
        ) {
            return
        }
        if (footHeight >= DisplayUtil.cal.dp2Px(
                context,
                loadMore.toFloat()
            )
        ) {
            return
        }
        headMaxHeight =
            DisplayUtil.cal.dp2Px(context, refresh.toFloat())
        footMaxHeight =
            DisplayUtil.cal.dp2Px(context, loadMore.toFloat())
    }

    //endregion
}