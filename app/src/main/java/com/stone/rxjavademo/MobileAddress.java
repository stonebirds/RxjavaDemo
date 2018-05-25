package com.stone.rxjavademo;

class MobileAddress<T> {

    /**
     * error_code : 10005
     * reason : 应用未审核超时，请提交认证
     */

    private int error_code;
    private String reason;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
