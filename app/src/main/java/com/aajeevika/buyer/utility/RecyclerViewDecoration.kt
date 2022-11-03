package com.aajeevika.buyer.utility

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDecoration(
    private val start: Float,
    private val top: Float,
    private val end: Float,
    private val bottom: Float
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = UtilityActions.pxFromDp(view.context, start).toInt()
        outRect.top = UtilityActions.pxFromDp(view.context, top).toInt()
        outRect.right = UtilityActions.pxFromDp(view.context, end).toInt()
        outRect.bottom = UtilityActions.pxFromDp(view.context, bottom).toInt()
    }
}