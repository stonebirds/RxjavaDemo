package com.stone.commom.mvp;

public abstract class BaseMvpActivity<V, T extends BasePresenterImpl> extends BaseActivity {

    T mPresenter;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        mPresenter = createPresenter();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    protected abstract T createPresenter();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.onDestroy();
        }
    }
}
