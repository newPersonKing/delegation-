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
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;



public abstract class AdapterDelegate<T, VH extends RecyclerView.ViewHolder> {
    /*每一个委托adapter的唯一标识*/
    public static final String DEFAULT_TAG = "";

    private String mTag = DEFAULT_TAG;

    public AdapterDelegate() {
    }

    public AdapterDelegate(@NonNull String tag) {
        if (null == tag || tag.length() == 0) {
            throw new NullPointerException("The tag of "
                    + this
                    + " is null.");
        }
        this.setTag(tag);
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }


    protected boolean isForViewType(T item, int position) {
        return true;
    }


    protected abstract VH onCreateViewHolder(ViewGroup parent);


    protected abstract void onBindViewHolder(VH holder, int position, T item);


    protected void onBindViewHolder(VH holder, int position, List<Object> payloads, T item) {
    }


    protected void onViewRecycled(VH holder) {
    }


    protected boolean onFailedToRecycleView(VH holder) {
        return false;
    }


    protected void onViewAttachedToWindow(VH holder) {
    }


    protected void onViewDetachedFromWindow(VH holder) {
    }
}
