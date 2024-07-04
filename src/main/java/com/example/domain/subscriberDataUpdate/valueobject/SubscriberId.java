package com.example.domain.subscriberDataUpdate.valueobject;

import com.example.domain.subscriberDataUpdate.common.Result;

import java.util.regex.Pattern;

public class SubscriberId implements ValueObject<String> {
    private final String value;

    private static final Pattern isStringConsist6Digits = Pattern.compile("^\\d{1,6}$");

    private SubscriberId(String value) {
        this.value = value;
    }

    public static Result<SubscriberId> emerge(String subscriberId) {
        if (isStringConsist6Digits(subscriberId)) {
            return Result.success(new SubscriberId(subscriberId));
        } else {
            return Result.failure(new IllegalArgumentException("Subscriber Id consists of numbers maximum length 6"));
        }
    }

    private static boolean isStringConsist6Digits(String value) {
        return isStringConsist6Digits.matcher(value).matches();
    }

    @Override
    public String value() {
        return this.value;
    }
}
