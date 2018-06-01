package com.gy.mydelegationadapterjava;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.gy.delegationadapter.AdapterDelegate;
import com.gy.delegationadapterimpl.BindCallBack;

import adpterimpl.BaseViewHolder;
import bean.News;

/*只需要修改不同的数据类型 就可以进行不同的页面展示*/
public class MorePicAdapterDelegate<T> extends AdapterDelegate<T, BaseViewHolder> {

    private int layoutId;

    private BindCallBack callBack;

    public MorePicAdapterDelegate(int layoutId,BindCallBack<T> callBack){
        this.layoutId=layoutId;
        this.callBack=callBack;
    }
    /*用来进行同样的数据源不同的显示效果*/
    @Override
    protected boolean isForViewType(T news, int position) {
        // 我能处理多张图片
//        return news.type == 2;
        return true;
    }

    @Override
    protected BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_more_pic, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, int position, T news) {
        callBack.onBind(holder,position,news);
    }

}
