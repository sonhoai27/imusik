package com.sonhoai.sonho.imusik.Interface;

public interface CallBack<T> {
    public void onSuccess(T result);
    public void onFail(T result);
}
