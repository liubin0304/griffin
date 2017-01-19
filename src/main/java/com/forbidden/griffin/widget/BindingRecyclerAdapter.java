package com.forbidden.griffin.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MVVM数据绑定的Recycler适配器
 */
public abstract class BindingRecyclerAdapter<Entity, VH extends BindingRecyclerAdapter.BindingHolder> extends RecyclerView.Adapter<VH> {
    private List<Entity> mList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private VH mViewHolder;
    /**
     * 被选中的Item
     */
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();

    public BindingRecyclerAdapter(Collection<Entity> entities) {
        if (entities == null) {
            return;
        }
        mList.addAll(entities);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        mViewHolder = holder;
        mContext = holder.itemView.getContext();
        holder.itemView.setActivated(mSelectedItems.get(position, false));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(BindingRecyclerAdapter.this, v, position, getItemId(position));
                }
            }
        });
    }

    protected Context getContext() {
        return mContext;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 切换位置上的状态
     *
     * @param pos
     */
    public void toggleSelection(int pos) {
        if (pos < 0) {
            return;
        }
        if (mSelectedItems.get(pos, false)) {
            mSelectedItems.delete(pos);
        } else {
            mSelectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 清除所有选中
     */
    public void clearSelections() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    /**
     * 返回被选中的Item的数量
     *
     * @return
     */
    public int getSelectedItemCount() {
        return mSelectedItems.size();
    }

    /**
     * 返回被选中的项目的位置
     *
     * @return
     */
    public List<Integer> getSelectedIndexes() {
        List<Integer> items = new ArrayList<Integer>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            items.add(mSelectedItems.keyAt(i));
        }
        return items;
    }

    public List<Entity> getSelectedItems() {
        List<Entity> list = new ArrayList<>();
        for (int i = 0; i < mSelectedItems.size(); ++i) {
            list.add(getItem(mSelectedItems.keyAt(i)));
        }
        return list;
    }

    /**
     * 返回位置上的项目
     *
     * @param position
     * @return
     */
    public Entity getItem(int position) {
        if (mList == null || position < 0 || position >= mList.size()) {
            return null;
        }
        return mList.get(position);
    }

    /**
     * 删除一个项目
     *
     * @param position
     */
    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 清除全部
     */
    public void clearItems() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 更新一个项目
     *
     * @param position
     * @param entity
     */
    public void updateItem(int position, Entity entity) {
        if (entity == null) {
            return;
        }
        mList.set(position, entity);
        notifyItemChanged(position);
    }

    /**
     * 返回数据
     *
     * @return
     */
    public List<Entity> getListData() {
        return mList;
    }

    /**
     * 添加项目
     *
     * @param entity
     */
    public void addItem(Entity entity) {
        if (entity == null) {
            return;
        }
        mList.add(entity);
        notifyItemInserted(mList.size());
    }

    /**
     * 添加到最前面
     *
     * @param entities
     */
    public void addFirstItems(Collection<Entity> entities) {
        if (entities == null) {
            return;
        }
        mList.addAll(0, entities);
        notifyItemRangeInserted(0, entities.size());
    }

    public void addFirstItem(Entity entities) {
        if (entities == null) {
            return;
        }
        mList.add(0, entities);
        notifyItemRangeInserted(0, 1);
    }

    /**
     * 添加多个项目
     *
     * @param entities
     */
    public void addItems(Collection<Entity> entities) {
        if (entities == null) {
            return;
        }
        final int start = mList.size();
        mList.addAll(entities);
        notifyItemRangeInserted(start, entities.size());
    }

    public void setVariable(int variableId, Object value) {
        if (mViewHolder != null) {
            mViewHolder.getBinding().setVariable(variableId, value);
            mViewHolder.getBinding().executePendingBindings();
        }
    }

    public static class BindingHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(View root) {
            super(root);
            binding = DataBindingUtil.bind(root);
        }

        public T getBinding() {
            return (T) binding;
        }
    }
}
