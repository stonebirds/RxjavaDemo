package com.stone.commom.mvp;

import java.lang.ref.WeakReference;

public abstract class BasePresenterImpl<V extends IBaseView> implements IBasePresenter<V> {
    protected WeakReference<V> mView;

    @Override
    public V getView() {
        return mView.get();
    }

    @Override
    public void attachView(V view) {
        mView = new WeakReference<V>(view);
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
        onDestroy();
    }

    protected abstract void onDestroy();
}
