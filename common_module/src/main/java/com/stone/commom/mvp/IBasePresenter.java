package com.stone.commom.mvp;

public interface IBasePresenter<V extends IBaseView> {
    V getView();

    void attachView(V view);

    void detachView();

}
