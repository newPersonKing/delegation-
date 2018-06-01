/*
 * Copyright (c) 2018 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gy.delegationadapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class AdapterDelegatesManager<VH extends RecyclerView.ViewHolder> {

    private SparseArray<String> dataTypeWithTags = new SparseArray<>();
    /*存储所有的委托 adapter*/
    private SparseArrayCompat<AdapterDelegate<Object, VH>> delegates = new SparseArrayCompat();

    protected AdapterDelegate fallbackDelegate;

    /*取消通过反射制造tag的步骤 直接*/
    public AdapterDelegatesManager addDelegate(AdapterDelegate<Object, VH> delegate, String tag) {

        if (!tag.isEmpty()) {

            /*所有添加委托的adapter的每一个数据源type 与tag*/
//            String typeWithTag = getTypeWithTag(clazz, tag);
            String typeWithTag=tag;

            int viewType = delegates.size();
            /*存储每一个委托对应的下标值 以及对应的委托*/
            delegates.put(viewType, delegate);
            /*存储每一个委托对应的下标值 与每一个委托的数据源type与tag*/
            dataTypeWithTags.put(viewType, typeWithTag);
            Log.i("ccccccccccccc","typeWithTag2222222"+typeWithTag);
        } else {

            throw new IllegalArgumentException(
                    String.format("Please set the correct generic parameters on %s.", delegate.getClass().getName()));
        }
        return this;
    }

    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterDelegate<Object, VH> delegate = getDelegate(viewType);
        if (delegate == null) {
            throw new NullPointerException("No AdapterDelegate added for ViewType " + viewType);
        }

        VH vh = delegate.onCreateViewHolder(parent);
        if (vh == null) {
            throw new NullPointerException("ViewHolder returned from AdapterDelegate "
                    + delegate
                    + " for ViewType ="
                    + viewType
                    + " is null!");
        }
        return vh;
    }

    public void onBindViewHolder(VH holder, int position, Object item) {
        int viewType = holder.getItemViewType();
        AdapterDelegate<Object, VH> delegate = getDelegate(viewType);
        if (delegate == null) {
            throw new NullPointerException("No delegate found for item at position = "
                    + position
                    + " for viewType = "
                    + viewType);
        }
        delegate.onBindViewHolder(holder, position, item);
    }

    public void onBindViewHolder(VH holder, int position, List payloads, Object item) {
        int viewType = holder.getItemViewType();
        AdapterDelegate<Object, VH> delegate = getDelegate(viewType);
        if (delegate == null) {
            throw new NullPointerException("No delegate found for item at position = "
                    + position
                    + " for viewType = "
                    + viewType);
        }
        delegate.onBindViewHolder(holder, position, payloads, item);
    }


    /*也可以理解为获取对应的委托的adapter的下标值*/
    public int getItemViewType(@NonNull Object item, int position) {
        if (item == null) {
            throw new NullPointerException("Item data source is null.");
        }

        Class clazz = getTargetClass(item);
        String tag = getTargetTag(item);

//        String typeWithTag = getTypeWithTag(clazz, tag);
        String typeWithTag=clazz.getName();
        ArrayList<Integer> indexList = indexListOfValue(dataTypeWithTags, typeWithTag);
        if (indexList.size() > 0) {
            for (Integer index : indexList) {

                AdapterDelegate<Object, VH> delegate = delegates.valueAt(index);
                if (null != delegate
                        && delegate.getTag().equals(tag)
                        && delegate.isForViewType(item, position)) {
                    return index;
                }
            }
        }

        // If has not add the AdapterDelegate for data type, returns the largest viewType + 1.
        /*没有给数据源配置 委托*/
        if (fallbackDelegate != null) {
            return delegates.size();
        }

        throw new NullPointerException("No AdapterDelegate added that matches position="
                + position + " item=" + item + " in data source.");
    }

    public void onViewRecycled(VH holder) {
        AdapterDelegate<Object, VH> delegate = getDelegate(holder.getItemViewType());
        if (delegate != null) {
            delegate.onViewRecycled(holder);
        }
    }

    public boolean onFailedToRecycleView(VH holder) {
        AdapterDelegate<Object, VH> delegate = getDelegate(holder.getItemViewType());
        if (delegate != null) {
            return delegate.onFailedToRecycleView(holder);
        }
        return false;
    }

    public void onViewAttachedToWindow(VH holder) {
        AdapterDelegate<Object, VH> delegate = getDelegate(holder.getItemViewType());
        if (delegate != null) {
            delegate.onViewAttachedToWindow(holder);
        }
    }

    public void onViewDetachedFromWindow(VH holder) {
        AdapterDelegate<Object, VH> delegate = getDelegate(holder.getItemViewType());
        if (delegate != null) {
            delegate.onViewDetachedFromWindow(holder);
        }
    }

    public AdapterDelegatesManager setFallbackDelegate(AdapterDelegate fallbackDelegate) {
        this.fallbackDelegate = fallbackDelegate;
        return this;
    }

    /**
     * Get the fallback delegate
     *
     * @return The fallback delegate or <code>null</code> if no fallback delegate has been set
     * @see #setFallbackDelegate(AdapterDelegate)
     */
    @Nullable
    public AdapterDelegate getFallbackDelegate() {
        return fallbackDelegate;
    }///fallbackDelegate 默认delete


    public AdapterDelegate<Object, VH> getDelegate(int viewType) {
        return delegates.get(viewType, fallbackDelegate);
    }

    /**
     * Returns the class name with tag;
     *
     * @param clazz
     * @param tag
     * @return
     */
    private String getTypeWithTag(Class clazz, String tag) {
        if (tag.length() == 0) {
            return clazz.getName();
        } else {
            return clazz.getName() + ":" + tag;
        }
    }

    /**
     * Returns the target class name
     *
     * @return
     */
    private Class getTargetClass(Object data) {
        return data instanceof ItemData ? ((ItemData) data).getData().getClass() : data.getClass();
    }

    /**
     * Returns the target tag
     *
     * @param data
     * @return
     */
    private String getTargetTag(Object data) {
        return data instanceof ItemData ? ((ItemData) data).getTag() : data.getClass().getName();
    }

    /**
     * Returns all indexes for the specified value
     *
     * @param array
     * @param value
     * @return
     */
    private ArrayList<Integer> indexListOfValue(SparseArray<String> array, String value) {
        ArrayList<Integer> indexList = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            if (value.equals(array.valueAt(i))) {
                indexList.add(i);
            }
        }
        return indexList;
    }
}
