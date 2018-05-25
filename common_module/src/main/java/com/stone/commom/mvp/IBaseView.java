package com.stone.commom.mvp;

public interface IBaseView {
    //显示进度对话框
    void showLoadDialog(String message);

    //隐藏进度对话框
    void hideLoadDialog();
}
