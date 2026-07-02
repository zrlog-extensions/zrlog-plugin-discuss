package com.zrlog.plugin.discuss.controller;

public class DiscussApiResponse<T> {

    private boolean success;
    private T data;

    public DiscussApiResponse() {
    }

    private DiscussApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public static DiscussApiResponse<Void> success() {
        return new DiscussApiResponse<Void>(true, null);
    }

    public static <T> DiscussApiResponse<T> success(T data) {
        return new DiscussApiResponse<T>(true, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
