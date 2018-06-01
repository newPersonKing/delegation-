package com.gy.delegationadapterimpl;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gy.delegationadapter.AdapterDelegate;

import adpterimpl.BaseViewHolder;
import bean.News;

public class Mydelegation extends AdapterDelegate<News, BaseViewHolder> {

    private int layoutId;

    private BindCallBack<News> callBack;

    public Mydelegation(int layoutId,BindCallBack<News> callBack){
        this.layoutId=layoutId;
        this.callBack=callBack;
    }

    @Override
    protected BaseViewHolder onCreateViewHolder(ViewGroup parent) {
       View view= LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        Log.i("cccccccccccc","onCreateViewHolder");
        return new BaseViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, int position, News item) {
        Log.i("cccccccccccc","onBindViewHolder");
      callBack.onBind(holder,position,item);
    }

    @Override
    protected boolean isForViewType(News item, int position) {
        return super.isForViewType(item, position);
    }
}
