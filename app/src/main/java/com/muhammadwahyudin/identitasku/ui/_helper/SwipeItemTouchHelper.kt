package com.muhammadwahyudin.identitasku.ui._helper

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class SwipeItemTouchHelper(var adapter: SwipeHelperAdapter) : ItemTouchHelper.Callback() {

    companion object {
        const val ALPHA_FULL = 1.0f
    }

    var bgColorCode: Int = Color.LTGRAY
    var deleteIcon: Drawable? = null
    var shareIcon: Drawable? = null
    private var intrinsicHeight = 0
    private var intrinsicWidth = 0
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return viewHolder.itemViewType == target.itemViewType
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            // Right to left
            ItemTouchHelper.START -> {
                Timber.d("Swiped start")
                adapter.onItemDismiss(viewHolder.adapterPosition)
            }
            // Left to right
            ItemTouchHelper.END -> {
                Timber.d("Swiped end")
                adapter.onItemShare(viewHolder.adapterPosition)
            }
        }
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val background = ColorDrawable(bgColorCode)
        background.color = bgColorCode

        val shareBackground = ColorDrawable(Color.parseColor("#4CAF50"))
        shareBackground.color = Color.parseColor("#4CAF50")

        val isCanceled = dX == 0f && !isCurrentlyActive

        // Reset background canvas if canceled
        if (isCanceled) {
            if (dX > 0) {
                clearCanvas(
                    c,
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    dX,
                    itemView.bottom.toFloat()
                )
            } else {
                clearCanvas(
                    c,
                    itemView.right + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw background
        if (dX > 0) {
            // Swipe to right
            shareBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            shareBackground.draw(c)
        } else {
            // Swipe to left
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            background.draw(c)
        }

        // Draw deleteIcon
        if (dX > 0) {
            shareIcon?.let {
                intrinsicHeight = it.intrinsicHeight
                intrinsicWidth = it.intrinsicWidth

                val iconMargin = (itemHeight - intrinsicHeight) / 2
                val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val iconBottom = iconTop + intrinsicHeight
                // Add on left
                it.setBounds(
                    itemView.left + iconMargin,
                    iconTop,
                    itemView.left + iconMargin + intrinsicWidth,
                    iconBottom
                )
                it.draw(c)
            }
        } else {
            deleteIcon?.let {
                intrinsicHeight = it.intrinsicHeight
                intrinsicWidth = it.intrinsicWidth

                val iconMargin = (itemHeight - intrinsicHeight) / 2
                val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val iconBottom = iconTop + intrinsicHeight
                // Add on right
                it.setBounds(
                    itemView.right - iconMargin - intrinsicWidth,
                    iconTop,
                    itemView.right - iconMargin,
                    iconBottom
                )
                it.draw(c)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    // From materialx, not currently used
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is TouchViewHolder) {
                val itemViewHolder = viewHolder as TouchViewHolder
                itemViewHolder.onItemSelected()
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    // From materialx, not currently used
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.alpha = ALPHA_FULL
        if (viewHolder is TouchViewHolder) {
            val itemViewHolder = viewHolder as TouchViewHolder
            itemViewHolder.onItemClear()
        }
    }

    interface SwipeHelperAdapter {
        fun onItemDismiss(position: Int)
        fun onItemShare(position: Int)
    }

    // From materialx, not currently used
    interface TouchViewHolder {
        fun onItemSelected()
        fun onItemClear()
    }
}