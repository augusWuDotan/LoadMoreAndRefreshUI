package com.wdta.loadmoreandrefreshui.base

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * @author augus
 * @create 2020-03-28
 * @Describe
 */
abstract class BaseAdapter(open var context: Context, open var mData: ArrayList<Any>? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val viewHolderFactory: ViewHolderFactory?
    var onAdapterItemListener: OnAdapterItemListener? = null
        set(value) {
            field = value
        }

    var recyclerDatas: ArrayList<Any>? = null


    init {
        if (mData == null) recyclerDatas = ArrayList()
        viewHolderFactory = ViewHolderFactory()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //viewType = ViewHolderConstant 常數 也是 layoutRes
        if (viewType == -1) {
            Log.d(context.packageName, "viewType is $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return viewHolderFactory!!.createViewHolder(viewType, view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = (holder as? BaseViewHolder<Any>)
        if (mHolder == null) {
            Log.d(context.packageName, "mHolder is $mHolder")
            return
        }
        mHolder.bindViewData(context, recyclerDatas!![position], position)
        if (onAdapterItemListener != null) {
            val clickViews = mHolder.getClickViews()
            var clickViewPosition = 0
            clickViews?.forEach {
                val itemViewPosition = clickViewPosition
                it.setOnClickListener {
                    onAdapterItemListener!!.onItemClick(it, itemViewPosition, position)
                }
                clickViewPosition++
            }
            //
            val longClickViews = mHolder.getClickViews()
            var longClickViewPosition = 0
            longClickViews?.forEach {
                val itemViewPosition = longClickViewPosition
                it.setOnLongClickListener {
                    onAdapterItemListener!!.onItemClick(it, itemViewPosition, position)
                    true
                }
                longClickViewPosition++
            }
            //
            onAdapterItemListener!!.onItemPositionScroll(position)
        }
        //辨認最後一個view
        if (position == recyclerDatas?.size!! - 1) {
            holder.bindFinalItem(context, recyclerDatas!![position], position)
        }
    }

    override fun getItemCount(): Int {
        return recyclerDatas!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = (recyclerDatas?.get(position)!! as? IItemLayoutRes)?.layoutRes ?: -1
//        Lcg.d("position:$position ,viewType:$viewType ")
//        Lcg.d("position:$position ,as IItemLayoutRes:${(recyclerDatas?.get(position)!! as IItemLayoutRes)} ")
//        Lcg.d("position:$position ,data:${recyclerDatas?.get(position)!!}")
        return (recyclerDatas?.get(position)!! as? IItemLayoutRes)?.layoutRes ?: -1
    }

    /**
     * 回傳header or footer的佈局
     *
     * @return
     */
    abstract fun itemHeadViewType(): Int

    abstract fun itemFootViewType(): Int

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val gridLayoutManager = manager
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val itemType = getItemViewType(position)
                    return if (itemType == itemHeadViewType() || itemType == itemFootViewType()) {
                        gridLayoutManager.spanCount
                    } else {
                        1
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams
            && holder.layoutPosition == 0
        ) {
            val p = lp as StaggeredGridLayoutManager.LayoutParams
            p.isFullSpan = true
        }
    }

    //region 資料處理

    //region 增加
    fun addData(data: Any) {
        recyclerDatas!!.add(data)
        notifyItemInserted(itemCount)
    }

    fun addData(inSertindex: Int, data: Any) {
        recyclerDatas!!.add(inSertindex, data)
        notifyItemInserted(inSertindex)
    }

    fun addDatas(datas: ArrayList<Any>) {
        recyclerDatas!!.addAll(datas)
        notifyItemInserted(itemCount)
    }

    fun addData(inSertindex: Int, datas: ArrayList<Any>) {
        if (recyclerDatas.isNullOrEmpty()) return
        if (itemCount < inSertindex) return
        recyclerDatas!!.addAll(inSertindex, datas)
        notifyItemInserted(inSertindex)
    }
    //endregion

    //region 刪除
    fun removeData(index: Int) {
        if (recyclerDatas.isNullOrEmpty()) return
        if (itemCount < index) return
        recyclerDatas!!.removeAt(index)
        notifyItemRemoved(index)
        notifyDataSetChanged()
    }
    //endregion

    //region 清除
    fun clear() {
        recyclerDatas!!.clear()
        notifyDataSetChanged()
    }
    //endregion

    //region 置換
    fun updateData(inSertindex: Int, data: Any) {
        recyclerDatas!![inSertindex] = data
        notifyDataSetChanged()
    }

    fun resetDatas(datas: ArrayList<Any>?) {
        if (datas != null) {
            recyclerDatas!!.clear()
            recyclerDatas!!.addAll(0, datas!!)
            notifyDataSetChanged()
        } else {
            recyclerDatas = ArrayList()
        }
    }
    //endregion

    //region 頭 ｜ 腳
    fun addHeadView(data: Any) {
        recyclerDatas!!.add(0, data)
        notifyItemInserted(0)
    }

    fun addFootView(data: Any) {
        recyclerDatas!!.add(itemCount, data)
        notifyItemInserted(itemCount)
    }
    //endregion

    //endregion

    /**
     * 取資料
     */
    fun getListData(): List<Any> {
        return recyclerDatas!!
    }

}
