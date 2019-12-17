package com.giphytest.ui.adapter

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View


class ItemOffsetDecoration(private val offSet: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes resourceId: Int) : this(context.resources.getDimensionPixelSize(resourceId)) {}

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(offSet, offSet, offSet, offSet)
    }
}
