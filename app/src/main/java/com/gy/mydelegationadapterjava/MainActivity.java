package com.gy.mydelegationadapterjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gy.delegationadapter.AbsDelegationAdapter;
import com.gy.delegationadapterimpl.BindCallBack;
import com.gy.delegationadapterimpl.Mydelegation;

import java.util.ArrayList;
import java.util.List;

import LocalFileUtil.LocalFileUtils;
import adpterimpl.BaseViewHolder;
import adpterimpl.MyAdapter;
import bean.News;
import bean.Text;
import bean.TextFooter;
import bean.TextHeader;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new MyAdapter();

        /*操作要领 数据源类型必须都换*/
        adapter.addDelegate(new MorePicAdapterDelegate(R.layout.item_news_more_pic, new BindCallBack<Text>() {
            @Override
            public void onBind(BaseViewHolder holder, int position, Text item) {
                holder.setText(R.id.tv_content,item.content);
            }
        }),Text.class.getName());

        adapter.addDelegate(new MorePicAdapterDelegate(R.layout.item_news_more_pic, new BindCallBack<News>() {
            @Override
            public void onBind(BaseViewHolder holder, int position, News item) {
                holder.setText(R.id.tv_content,item.content);
            }
        }),News.class.getName());

        adapter.addDelegate(new MorePicAdapterDelegate(R.layout.item_news_more_pic, new BindCallBack<TextHeader>() {
            @Override
            public void onBind(BaseViewHolder holder, int position, TextHeader item) {
                holder.setText(R.id.tv_content,item.content);
            }
        }),TextHeader.class.getName());

        adapter.addDelegate(new MorePicAdapterDelegate(R.layout.item_news_more_pic, new BindCallBack<TextFooter>() {
            @Override
            public void onBind(BaseViewHolder holder, int position, TextFooter item) {
                holder.setText(R.id.tv_content,item.content);
            }
        }),TextFooter.class.getName());

        adapter.setOnItemClickListener(new AbsDelegationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyAdapter adapter, View view, int position) {
                Toast.makeText(MainActivity.this,"position=="+position,Toast.LENGTH_SHORT).show();
            }
        });

        adapter.addHeaderItem(new TextHeader());
        adapter.addFooterItem(new TextFooter());

        recyclerView.setAdapter(adapter);
        initData();
        initData2();
    }

    private void initData() {
        String newsListStr = LocalFileUtils.getStringFormAsset(this, "news.json");
        List<News> newsList = new Gson().fromJson(newsListStr, new TypeToken<List<News>>() {
        }.getType());
        adapter.addDataItems(newsList);
    }

    private void initData2() {
        List<Text> list=new ArrayList<>();
        for (int i=0;i<100;i++){
            list.add(new Text());
        }
        adapter.addDataItems(list);
    }
}
