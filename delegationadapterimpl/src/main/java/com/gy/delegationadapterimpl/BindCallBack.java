package com.gy.delegationadapterimpl;

import adpterimpl.BaseViewHolder;

public interface BindCallBack<T> {

    void onBind(BaseViewHolder holder, int position, T item);

}
