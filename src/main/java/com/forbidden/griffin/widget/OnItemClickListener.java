package com.forbidden.griffin.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * List列表项点击事件
 */
public interface OnItemClickListener {
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position, long id);
}
