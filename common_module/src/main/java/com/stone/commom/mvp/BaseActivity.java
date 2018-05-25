package com.stone.commom.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {

    private View mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(mRootView);

        initView();
        initEvent();
        initData();
    }

    /**
     * 加载布局文件
     *
     * @return layout_id
     */
    public abstract int getLayoutId();

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 初始化事件
     */
    public abstract void initEvent();

    /**
     * 加载数据
     */
    public abstract void initData();
}
