package com.example.controller;

import com.example.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

public class RestResponse<T> {
    private final String status;
    private final long actualTimestamp;
    private T data;
    private RestError error;

    protected RestResponse(String status, long actualTimestamp, T data, RestError error) {
        this.status = status;
        this.actualTimestamp = actualTimestamp;
        this.data = data;
        this.error = error;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return data != null && "success".equals(status);
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    @JsonIgnore
    public T getValueOrThrow() {
        if (isSuccess()) {
            return data;
        } else {
            throw new RuntimeException("Can't get value here");
        }
    }

    @JsonIgnore
    public T getData() {
        return data;
    }

    @JsonIgnore
    public RestError getError() {
        return this.error;
    }

    public static <T> RestResponse<T> success(T data) {
        return new RestResponse<>("success", DateTimeUtils.nowToEpochMill(), data, null);
    }

    public static <T> RestResponse<T> fail(RestError error) {
        return new RestResponse<>("success", DateTimeUtils.nowToEpochMill(), null, error);
    }
}
