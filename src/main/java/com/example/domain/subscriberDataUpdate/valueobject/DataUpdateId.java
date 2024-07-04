package com.example.domain.subscriberDataUpdate.valueobject;

import com.example.domain.subscriberDataUpdate.common.Result;

public class DataUpdateId implements ValueObject<String> {
    private final String value;

    private DataUpdateId(String value) {
        this.value = value;
    }

    public static Result<DataUpdateId> emerge(String dataUpdateId) {
        return isStringWith9Digits(dataUpdateId)
                ? Result.success(new DataUpdateId(dataUpdateId))
                : Result.failure(new IllegalArgumentException("Data update Id consists of numbers maximum lenght 9"));
    }

    private static boolean isStringWith9Digits(String value) {
        return value.matches("[0-9]+");
    }

    @Override
    public String value() {
        return value;
    }
}
